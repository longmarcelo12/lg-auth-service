package com.example.lgauthservice.auth.presentation.models.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateTokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType;
}
