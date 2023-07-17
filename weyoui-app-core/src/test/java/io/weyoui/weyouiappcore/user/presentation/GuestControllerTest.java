package io.weyoui.weyouiappcore.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.user.presentation.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class GuestControllerTest {

    @Autowired
    MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("ID와 비밀번호가 유효성검사를 통과해야 회원가입이 가능하다")
    @Test
    void signUpValidate() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("tester0123@naver.com")
                .password("111111")
                .passwordConfirm("111111")
                .build();
        String apiUrl = "/api/v1/guest/sign-up";

        mvc.perform(
                post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                )
            .andDo(print())
            .andExpect(status().isOk());
    }


}