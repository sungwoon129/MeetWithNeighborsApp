package io.weyoui.weyouiappcore.user.presentation;

import io.weyoui.weyouiappcore.user.command.application.UserTokenService;
import io.weyoui.weyouiappcore.user.command.application.UserAuthService;
import io.weyoui.weyouiappcore.user.command.application.dto.LoginRequest;
import io.weyoui.weyouiappcore.user.command.application.dto.SignUpRequest;
import io.weyoui.weyouiappcore.user.query.application.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class IntegratedGuestControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserTokenService userTokenService;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    private final String grantType = "Bearer";

    @DisplayName("유효한 refresh 토큰으로 access 토큰을 재발급 받을 수 있다")
    @Test
    void tokenReissueTest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("testuser@weyoui.io")
                .password("123456")
                .passwordConfirm("123456")
                .build();
        userAuthService.signUp(signUpRequest);
        UserResponse.Token token = userTokenService.login(new LoginRequest(signUpRequest.getEmail(), signUpRequest.getPassword()));

        ResultActions resultActions = mvc.perform(post("/api/v1/guest/reissue")
                .header("Authorization",grantType + " " + token.getRefreshToken())
        ).andDo(print());

        assertTrue(resultActions.andReturn().getResponse().getContentAsString().contains(grantType));

        deleteToken(token.getRefreshToken());

    }

    private void deleteToken(String token) {
        redisTemplate.delete(token);
    }
}
