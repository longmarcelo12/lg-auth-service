package com.example.lgauthservice.shared.infrastructure.config;

import com.example.lgauthservice.shared.constants.Constants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .name(Constants.Headers.AUTHORIZATION)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(Constants.BEARER_TOKEN_PREFIX.toLowerCase().trim())
                                .bearerFormat("JWT")));
    }
}