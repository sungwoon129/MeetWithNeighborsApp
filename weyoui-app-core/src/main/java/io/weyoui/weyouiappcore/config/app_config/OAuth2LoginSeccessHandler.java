package io.weyoui.weyouiappcore.config.app_config;

import io.weyoui.weyouiappcore.user.command.application.UserTokenService;
import io.weyoui.weyouiappcore.user.infrastructure.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2LoginSeccessHandler implements AuthenticationSuccessHandler {
    public OAuth2LoginSeccessHandler(JwtTokenProvider jwtTokenProvider, UserTokenService userTokenService) {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
}
