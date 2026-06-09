package com.example.lgauthservice.auth.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secretKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
}
