package io.weyoui.weyouiappcore.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "WEYOUI App",
                description = "WEYOUI App API DEFINITION",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"api/v1/**"};

        return GroupedOpenApi.builder()
                .group("WEYOUI API v1")
                .pathsToMatch(paths)
                .build();
    }
}
