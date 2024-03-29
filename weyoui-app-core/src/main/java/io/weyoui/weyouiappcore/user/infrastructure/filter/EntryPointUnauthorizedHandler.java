package io.weyoui.weyouiappcore.user.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.common.exception.ErrorCode;
import io.weyoui.weyouiappcore.common.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse responseBody = ErrorResponse.of(ErrorCode.TOKEN_ERROR);
        responseBody.setHttpStatus(HttpStatus.UNAUTHORIZED);
        responseBody.setDetail("인증되지 않은 사용자의 요청입니다.");

        response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
