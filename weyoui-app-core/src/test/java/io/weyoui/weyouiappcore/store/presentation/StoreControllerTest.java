package io.weyoui.weyouiappcore.store.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.*;
import io.weyoui.weyouiappcore.store.infrastructure.StoreRepository;
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
import org.springframework.data.domain.Sort;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    StoreRepository storeRepository;

    @Autowired
    OwnerService ownerService;

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

        String api = "http://localhost:" + port + "/api/v1/users/store";

        //when
        MvcResult mvcResult = mvc.perform(post(api)
                        .header(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String test = mvcResult.getResponse().getContentAsString();
        CommonResponse<StoreId> response = objectMapper.readValue(test, new TypeReference<>() {
        });
        Store store = storeQueryRepository.findById(response.getData()).orElseThrow(() -> new IllegalArgumentException("가게 ID와 일치하는 가게가 DB에 존재하지않습니다."));
        assertEquals("내 가게", store.getName());
        assertEquals(userId, store.getOwner().getUserId());
        assertEquals("RESTAURANT", store.getCategory().name());
    }

    @DisplayName("요청한 클라이언트의 회원id와 가게의 오너의 회원id가 다른경우 BadRequest 응답을 반환한다")
    @Test
    void updateStoreValidation_test() throws Exception {
        //given
        UserId storeOwnerId = insertAnyUser();
        UserId anyUserId = insertAnyUser();

        StoreId storeId = insertAnyStore(ownerService.createOwner(storeOwnerId));

        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(anyUserId, "",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))), anyUserId);

        StoreRequest storeRequest = new StoreRequest();
        storeRequest.setName("my store");
        storeRequest.setState("B");
        storeRequest.setCategory(StoreCategory.SERVICE.getCode());

        String api = "http://localhost:" + port + "/api/v1/users/store/" + storeId.getId();


        //when,then
        mvc.perform(put(api)
                .header(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRequest))
        ).andExpect(status().isBadRequest());
    }

    @DisplayName("요청한 클라이언트의 회원id와 가게의 오너의 회원id가 일치하는 경우 가게 정보를 수정할 수 있다.")
    @Test
    void updateStore_test() throws Exception {
        //given
        UserId storeOwnerId = insertAnyUser();

        StoreId storeId = insertAnyStore(ownerService.createOwner(storeOwnerId));

        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(storeOwnerId, "",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))), storeOwnerId);

        StoreRequest storeRequest = new StoreRequest();
        storeRequest.setName("my store");
        storeRequest.setState("B");
        storeRequest.setCategory(StoreCategory.SERVICE.getCode());

        String api = "http://localhost:" + port + "/api/v1/users/store/" + storeId.getId();


        //when
        mvc.perform(put(api)
                .header(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRequest))
        ).andExpect(status().isOk());

        //then
        Store store = storeQueryRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("id와 일치하는 가게가 존재하지 않습니다."));
        assertEquals(storeRequest.getName(), store.getName());
        assertEquals(storeRequest.getState(), store.getState().getCode());
        assertEquals(storeRequest.getCategory(), store.getCategory().getCode());

    }

    private UserId insertAnyUser() {
        UserId userId = userRepository.nextUserId();
        userRepository.save(
                User.builder()
                        .id(userId)
                        .nickname("any user")
                        .build());

        return userId;
    }

    private StoreId insertAnyStore(Owner owner) {
        StoreId storeId = storeRepository.nextId();
        storeRepository.save(
                Store.builder()
                        .storeId(storeId)
                        .name("any store")
                        .category(StoreCategory.SERVICE)
                        .owner(owner)
                        .build()
        );

        return storeId;
    }

    private UserId getFirstUserIdFromDb() {

        Page<User> result = userQueryRepository.findByConditions(new UserSearchRequest(), PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id"))));

        return result.getContent().stream().findFirst().orElseThrow(() -> new NotFoundUserException("no user")).getId();
    }

}