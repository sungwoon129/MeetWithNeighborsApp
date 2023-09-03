package io.weyoui.weyouiappcore.store.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.product.command.domain.ProductId;
import io.weyoui.weyouiappcore.product.query.application.dto.ProductViewResponse;
import io.weyoui.weyouiappcore.store.command.domain.StoreId;
import io.weyoui.weyouiappcore.store.query.application.dto.StoreViewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}