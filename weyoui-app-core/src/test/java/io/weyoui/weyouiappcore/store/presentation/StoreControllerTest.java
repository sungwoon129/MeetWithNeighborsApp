package io.weyoui.weyouiappcore.store.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.model.Address;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.store.command.application.dto.StoreRequest;
import io.weyoui.weyouiappcore.store.command.domain.Store;
import io.weyoui.weyouiappcore.store.command.domain.StoreCategory;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import io.weyoui.weyouiappcore.store.query.infrastructure.StoreQueryRepository;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:store-init-test.sql")
class StoreControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
        UserId userId = new UserId("user1");

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
        UserId anyUserId = new UserId("user2");

        StoreId storeId = new StoreId("store1");

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
        UserId storeOwnerId = new UserId("user1");
        StoreId storeId = new StoreId("store1");

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

    @WithMockUser
    @DisplayName("이름,상태,주소,거리를 가지고 가게를 검색하여 목록을 반환한다.")
    @Test
    void getStoreViewListTest() throws Exception{

        //given
        StoreId storeId = new StoreId("store1");

        String api = "http://localhost:" + port + "/api/v1/users/stores";

        //when
        MvcResult result = mvc.perform(get(api)
                        .queryParam("page","0")
                        .queryParam("size","100")
                        .queryParam("name","가게")
                        .queryParam("states","O")
                        .queryParam("location.latitude","37.37362247078315")
                        .queryParam("location.longitude","127.11302379211246")
                        .queryParam("distance","3")
                        .queryParam("sort","id,desc")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //then
        CommonResponse<List<StoreViewResponse>> list = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        Store store = storeQueryRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("일치하는 가게가 존재하지 않습니다."));

        assertTrue("검색결과내에 ID가 일치하는 가게가 존재하지 않습니다.", list.getData().stream().anyMatch(storeViewResponse -> storeViewResponse.getStoreId().equals(store.getId())));
    }
}