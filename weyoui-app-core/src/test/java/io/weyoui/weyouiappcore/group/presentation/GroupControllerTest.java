package io.weyoui.weyouiappcore.group.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.weyoui.weyouiappcore.common.CommonResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupAddResponse;
import io.weyoui.weyouiappcore.group.command.application.dto.GroupRequest;
import io.weyoui.weyouiappcore.group.command.domain.Group;
import io.weyoui.weyouiappcore.group.command.domain.GroupCategory;
import io.weyoui.weyouiappcore.group.command.domain.GroupId;
import io.weyoui.weyouiappcore.group.command.domain.GroupState;
import io.weyoui.weyouiappcore.group.query.infrastructure.GroupQueryRepository;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MockMvc mvc;

    @Autowired
    GroupQueryRepository groupQueryRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserQueryRepository userQueryRepository;

    @Autowired
    UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }



    @DisplayName("클라이언트의 요청대로 그룹이 생성된다")
    @Test
    void createGroupTest() throws Exception {
        //given
        insertAnyUser();
        UserId userId = getFirstUserIdFromDb();

        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userId,"",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))),userId);


        GroupRequest groupRequest = GroupRequest.builder()
                .name("운동 모임")
                .category(GroupCategory.WORKOUT.name())
                .capacity(5)
                .description("공원에서 운동해요!")
                .startTime(LocalDateTime.now().minusMinutes(10))
                .endTime(LocalDateTime.now().plusHours(1))
                // TODO : Point 객체 생성해서 검색하는 테스트코드 작성필요
                //.venue(new Address("서울","한강","123-456",new Point(37.5650407,126.8858048)))
                .build();

        String apiUrl = "http://localhost:" + port + "/api/v1/users/group";

        //when
        MvcResult result = mvc.perform(post(apiUrl)
                                    .header(HttpHeaders.AUTHORIZATION,token.getGrantType() + " " + token.getAccessToken())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(groupRequest))
                                    .with(csrf()))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn();

        //then
        CommonResponse<GroupAddResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        Group group = groupQueryRepository.findById(new GroupId(response.getData().getGroupId())).orElseThrow(() -> new IllegalArgumentException("아이디와 일치하는 그룹이 없습니다."));

        assertEquals(group.getName(),groupRequest.getName());
        assertEquals(group.getCapacity(),groupRequest.getCapacity());
        assertEquals(group.getDescription(),groupRequest.getDescription());
        assertEquals(group.getVenue().getFullAddress(),groupRequest.getVenue().getFullAddress());
        assertEquals(group.getVenue().getZipCode(),groupRequest.getVenue().getZipCode());
        assertEquals(group.getVenue().getPoint().getX(),groupRequest.getVenue().getPoint().getX());
        assertEquals(group.getVenue().getPoint().getY(),groupRequest.getVenue().getPoint().getY());
        assertEquals(group.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")),groupRequest.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")));
        assertEquals(group.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")),groupRequest.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm")));

        assertEquals(group.getState(), GroupState.IN_ACTIVITY);

    }

    private void insertAnyUser() {
        userRepository.save(
                User.builder()
                        .id(userRepository.nextUserId())
                        .nickname("any user")
                        .build());
    }

    private UserId getFirstUserIdFromDb() {
        Page<User> result = userQueryRepository.findByConditions(new UserSearchRequest(), PageRequest.of(0,1));
        
        return result.getContent().stream().findFirst().orElseThrow(() -> new NotFoundUserException("no user")).getId();
    }

}
