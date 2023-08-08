package io.weyoui.weyouiappcore.user.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.weyoui.weyouiappcore.config.exception.ErrorCode;
import io.weyoui.weyouiappcore.config.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponse responseBody = ErrorResponse.of(ErrorCode.INVALID_RESOURCE_ACCESS);
        responseBody.setHttpStatus(HttpStatus.FORBIDDEN);
        responseBody.setDetail("인가받지 못한 자원에 접근할 수 없습니다.");

        response.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
        response.getWriter().close();

    }
}
