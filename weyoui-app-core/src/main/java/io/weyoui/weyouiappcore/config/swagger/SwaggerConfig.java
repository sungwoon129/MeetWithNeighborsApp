package io.weyoui.weyouiappcore.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@SecurityScheme(
        name = HttpHeaders.AUTHORIZATION,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer",
        description = "API 접근을 위해 JWT 토큰이 필요합니다. 토큰은 GUEST API의 로그인 API에 올바른 email과 password를 전달하면 얻을 수 있습니다."
)
@OpenAPIDefinition(
        info = @Info(title = "WEYOUI App",
                description = "WEYOUI App API 명세",
                version = "v1"),
        servers = @Server(
                    url = "${api.server.baseUrl}",
                    description = "${api.server.description}"
                    )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openGuestApi() {
        String[] paths = {"/api/v1/guest/**"};

        return GroupedOpenApi.builder()
                .group("WEYOUI GUEST API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi openUserApi() {
        String[] paths = {"/api/v1/users/**"};

        return GroupedOpenApi.builder()
                .group("WEYOUI USER API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi openAdminApi() {
        String[] paths = {"/api/v1/admin/**"};

        return GroupedOpenApi.builder()
                .group("WEYOUI ADMIN API v1")
                .pathsToMatch(paths)
                .build();
    }
}
