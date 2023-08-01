package io.weyoui.weyouiappcore.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.config.app_config.SecurityConfig;
import io.weyoui.weyouiappcore.user.command.application.UserAuthService;
import io.weyoui.weyouiappcore.user.command.application.UserTokenService;
import io.weyoui.weyouiappcore.user.command.application.dto.LoginRequest;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class})
@AutoConfigureMockMvc
@WebMvcTest(GuestController.class)
@ExtendWith(MockitoExtension.class)
class GuestControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserTokenService userTokenService;
    @MockBean
    UserAuthService userAuthService;
    @MockBean
    JwtTokenProvider jwtTokenProvider;
    ObjectMapper objectMapper = new ObjectMapper();


    @DisplayName("ID와 비밀번호가 유효성검사를 통과해야 회원가입이 가능하다")
    @WithAnonymousUser
    @Test
    void signUpValidate() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("tester0123@naver.com")
                .password("111111")
                .passwordConfirm("111111")
                .build();
        String apiUrl = "/api/v1/guest/sign-up";

        mvc.perform(post(apiUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @DisplayName("로그인을 성공하면 토큰이 발급된다")
    @Test
    void issueTokenTest() throws Exception {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("tester@weyoui.com")
                .password("123456")
                .build();
        UserResponse.Token token = UserResponse.Token.builder()
                .accessToken("auifeui")
                .refreshToken("efioaeio")
                .grantType("Bearer")
                .refreshTokenExpirationTime(12391034324L)
                .build();
        when(userTokenService.login(any(LoginRequest.class))).thenReturn(token);

        //when
        MvcResult result = mvc.perform(post("/api/v1/guest/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        assertTrue(result.getResponse().getContentAsString().contains("Bearer"));
        assertTrue(result.getResponse().getContentAsString().contains("accessToken"));
        assertTrue(result.getResponse().getContentAsString().contains("refreshToken"));
        assertTrue(result.getResponse().getContentAsString().contains("refreshTokenExpirationTime"));

    }
}