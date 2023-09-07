package io.weyoui.weyouiappcore.store.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.product.command.application.dto.FileInfo;
import io.weyoui.weyouiappcore.product.command.domain.Product;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.product.query.infrastructure.ProductQueryRepository;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import io.weyoui.weyouiappcore.user.command.domain.RoleType;
import io.weyoui.weyouiappcore.user.command.domain.UserId;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:store-init-test.sql")
class StoreProductsControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    JtsModule jtsModule;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ProductQueryRepository productQueryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(jtsModule);
    }



    @WithMockUser
    @DisplayName("가게 ID를 가지고 가게 정보 API를 요청하면 상품 목록,상품 이미지 목록등 가게 상세정보를 반환한다")
    @Test
    void findByIdInDetail_test() throws Exception {
        //given
        StoreId storeId = new StoreId("store1");
        ProductId productId = new ProductId("product1");


        String api = "http://localhost:" + port + "/api/v1/users/store/" + storeId.getId();

        //when
        MvcResult result = mvc.perform(get(api))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //then
        CommonResponse<StoreViewResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        ProductViewResponse productViewResponse = response.getData().getProductInfos().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID와 일치하는 상품이 존재하지 않습니다."));

        assertThat(productViewResponse.getId()).isEqualTo(productId);
        assertThat(productViewResponse.getImages().size()).isGreaterThan(1);


    }

    @WithMockUser
    @DisplayName("상품의 이미지를 여러개 등록할 수 있다.")
    @Test
    void whenMultipleFileUploaded_thenVerifyStatus() throws Exception {
        //given
        ProductId productId = new ProductId("product1");
        URL resource = getClass().getResource("/404.png");
        assert resource != null;
        FileInputStream inputStream = new FileInputStream(resource.getPath());

        MockMultipartFile file1 = new MockMultipartFile("files", "404.png", MediaType.IMAGE_PNG_VALUE, inputStream);

        List<FileInfo> fileInfos = new ArrayList<>();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setStorageType("INTERNAL");
        fileInfo.setUpdateListIdx(2);
        fileInfos.add(fileInfo);


        String fileInfosJson = objectMapper.writeValueAsString(fileInfos);
        MockMultipartFile fileInfosFile = new MockMultipartFile("fileInfos", "fileInfos", MediaType.APPLICATION_JSON.toString(), fileInfosJson.getBytes(StandardCharsets.UTF_8));

        String apiUrl = "http://localhost:" + port + "/api/v1/users/store/product/" + productId.getId();


        UserId userId = new UserId("user1");
        UserResponse.Token token = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userId, "",
                Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()))), userId);

        //when,then
        mvc.perform(multipart(apiUrl)
                .file(file1)
                .file(fileInfosFile)
                .header(HttpHeaders.AUTHORIZATION, token.getGrantType() + " " + token.getAccessToken())
        ).andDo(print()).andExpect(status().isCreated());
    }

    @WithMockUser
    @DisplayName("상품의 아이디로 상세정보를 조회한다")
    @Test
    void getDetailInfoByProductId_test() throws Exception {
        //given
        ProductId productId = new ProductId("product1");

        String apiUrl = "http://localhost" + port + "/api/v1/users/store/product/" + productId.getId();

        //when
        MvcResult result = mvc.perform(get(apiUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        CommonResponse<ProductViewResponse> viewResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(viewResponse.getData().getImages().size()).isGreaterThan(0);

        // Notice : query 2번실행
    }
}