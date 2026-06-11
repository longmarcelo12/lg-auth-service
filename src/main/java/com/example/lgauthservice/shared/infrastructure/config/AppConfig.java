package com.example.lgauthservice.shared.infrastructure.config;

import com.example.lgauthservice.auth.infrastructure.config.JwtProperties;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "spring.application")
@Data
@Validated
@EnableConfigurationProperties({JwtProperties.class})
public class AppConfig {
    public static String SERVICE_NAME;
    public static boolean ENV_PRODUCT;
    @NotBlank
    private String name;
    @NotNull
    private boolean product;

    @PostConstruct
    public void init() {
        SERVICE_NAME = this.name;
        ENV_PRODUCT = this.product;
    }
}
