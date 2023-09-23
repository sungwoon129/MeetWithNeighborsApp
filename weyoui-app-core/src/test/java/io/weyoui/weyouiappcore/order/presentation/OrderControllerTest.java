package io.weyoui.weyouiappcore.order.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.weyoui.weyouiappcore.common.exception.ErrorCode;
import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import io.weyoui.weyouiappcore.common.model.CommonResponse;
import io.weyoui.weyouiappcore.order.query.application.dto.OrderViewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("classpath:store-init-test.sql")
@SpringBootTest
class OrderControllerTest {

    @Autowired
    MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("주문 ID로 주문정보를 얻을 수 있다.")
    @Test
    @WithMockUser
    void findById_test_success() throws Exception {
        //given
        String orderId = "order1";
        String url = "/api/v1/users/order/" + orderId;
        String expectedOrdererName = "임의의 모임";
        int expectedOrderAmounts = 30000;

        //when
        MvcResult result = mvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        CommonResponse<OrderViewResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {});
        assertThat(response.getData().getOrderer().getName()).isEqualTo(expectedOrdererName);
        assertThat(response.getData().getTotalAmounts().getValue()).isEqualTo(expectedOrderAmounts);
    }

    @DisplayName("유효하지 않은 주문 ID로 주문 정보 API 요청 시 예외정보를 클라이언트에 반환한다.")
    @Test
    @WithMockUser
    void findById_test_fail() throws Exception {
        //given
        String orderId = "invalid OrderId.";
        String url = "/api/v1/users/order/" + orderId;

        //when
        MvcResult result = mvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {});
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getDetail()).contains("ID와 일치하는 주문 정보가 존재하지 않습니다.");
        assertThat(response.getCode()).isEqualTo(ErrorCode.ILLEGAL_ARGUMENT.getCode());
    }

}