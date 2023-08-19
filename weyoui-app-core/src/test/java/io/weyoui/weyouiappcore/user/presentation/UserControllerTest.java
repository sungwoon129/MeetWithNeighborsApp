package io.weyoui.weyouiappcore.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.weyoui.weyouiappcore.user.command.application.UserAuthService;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import io.weyoui.weyouiappcore.user.command.application.dto.UserUpdateRequest;
import io.weyoui.weyouiappcore.user.command.application.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @LocalServerPort
    int port;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserQueryRepository userQueryRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @WithMockUser
    @Test
    void findByIdTest() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("exUser@weyoui.com")
                .password("123456")
                .passwordConfirm("123456")
                .build();

        UserId id = userAuthService.signUp(signUpRequest);
        String uri = "http://localhost:" + port + "/api/v1/users/" + id.getId();


        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void changeUserInfo() throws Exception {
        //given
        insertAnyUser();
        UserId userId = getFirstUserIdFromDb();

        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userId, "",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))), userId);

        String api = "http://localhost:" + port + "/api/v1/users/me/identification";
        String expectedIsIdentified = "Y";
        LocalDateTime expectedIdentificationDate = LocalDateTime.now();

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .isIdentified(expectedIsIdentified)
                .identificationDate(expectedIdentificationDate)
                .build();

        //when
        mvc.perform(put(api)
                        .header(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                ).andDo(print())
                .andExpect(status().isOk());

        //then
        User user = userQueryRepository.findById(userId).orElseThrow(() -> new NotFoundUserException("회원이 존재하지 않습니다."));
        assertEquals(expectedIdentificationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")), user.getIdentificationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        assertTrue(user.isIdentified());

    }


    private void insertAnyUser() {
        userRepository.save(
                User.builder()
                        .id(userRepository.nextUserId())
                        .nickname("any user")
                        .build());
    }

    private UserId getFirstUserIdFromDb() {
        Page<User> result = userQueryRepository.findByConditions(new UserSearchRequest(), PageRequest.of(0, 1));

        return result.getContent().stream().findFirst().orElseThrow(() -> new NotFoundUserException("no user")).getId();
    }


}