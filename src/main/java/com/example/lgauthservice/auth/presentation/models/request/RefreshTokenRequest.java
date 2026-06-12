package com.example.lgauthservice.auth.presentation.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "refreshToken is not empty")
    private String refreshToken;
}
