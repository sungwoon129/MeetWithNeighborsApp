package io.weyoui.weyouiappcore.config.app_config;

import io.weyoui.weyouiappcore.config.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SecurityConfigTest {

    @Autowired
    MockMvc mvc;

    @DisplayName("인증받지 못한 사용자의 요청은 거절하고 메시지를 반환한다.")
    @Test
    void testAuthenticate() throws Exception {
        //given
        String apiUri = "/api/v1/admin/users";

        //when
        MvcResult mvcResult = mvc.perform(get(apiUri)).andDo(print()).andReturn();

        //then
        assertTrue(mvcResult.getResponse().getContentAsString().contains(ErrorCode.TOKEN_ERROR.getMessage()));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(HttpStatus.UNAUTHORIZED.name()));

    }

    @WithMockUser(roles = "{USER}")
    @DisplayName("인가받지 못한 사용자는 권한이 없는 자원에 접근할 수 없다.")
    @Test
    void testAuthorize() throws Exception {
        //given
        String apiUri = "/api/v1/admin/users";

        //when
        MvcResult mvcResult = mvc.perform(get(apiUri)).andDo(print()).andReturn();

        //then
        assertTrue(mvcResult.getResponse().getContentAsString().contains(ErrorCode.INVALID_RESOURCE_ACCESS.getMessage()));
        assertTrue(mvcResult.getResponse().getContentAsString().contains(HttpStatus.FORBIDDEN.name()));
    }



}