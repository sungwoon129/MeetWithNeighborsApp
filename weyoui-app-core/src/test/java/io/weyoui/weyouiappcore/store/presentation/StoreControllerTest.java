package io.weyoui.weyouiappcore.store.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.infrastructure.StoreQueryRepository;
import io.weyoui.weyouiappcore.user.command.application.exception.NotFoundUserException;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.User;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.infrastructure.UserRepository;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import io.weyoui.weyouiappcore.user.query.application.dto.UserSearchRequest;
import io.weyoui.weyouiappcore.user.query.infrastructure.UserQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.n52.jackson.datatype.jts.JtsModule;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserQueryRepository userQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreQueryRepository storeQueryRepository;

    @Autowired
    JtsModule jtsModule;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(jtsModule);
    }


    @DisplayName("클라이언트의 요청대로 가게가 생성된다")
    @Test
    void createStoreTest() throws Exception {
        //given
        insertAnyUser();
        UserId userId = getFirstUserIdFromDb();

        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userId, "",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))), userId);

        StoreRequest storeRequest = new StoreRequest();
        storeRequest.setName("내 가게");
        storeRequest.setCategory("FOOD");
        storeRequest.setAddress(new Address(
                "대한민국 경기도",
                "성남시 분당구",
                "123-456",
                new GeometryFactory().createPoint(new Coordinate(38.37369668919236, 128.11314527061833)))
        );

        String api = "http://localhost:" + port +  "/api/v1/users/store";

        //when
        MvcResult mvcResult = mvc.perform(post(api)
                        .header(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRequest))
                ).andExpect(status().isOk())
                .andReturn();

        //then
        String test = mvcResult.getResponse().getContentAsString();
        CommonResponse<StoreId> response = objectMapper.readValue(test, new TypeReference<>() {});
        Store store = storeQueryRepository.findById(response.getData()).orElseThrow(() -> new IllegalArgumentException("가게 ID와 일치하는 가게가 DB에 존재하지않습니다."));
        assertEquals("내 가게", store.getName());
        assertEquals(userId, store.getOwner().getUserId());
        assertEquals("RESTAURANT", store.getCategory().name());
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