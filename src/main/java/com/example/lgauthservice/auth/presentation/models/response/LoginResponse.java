package com.example.lgauthservice.auth.presentation.models.response;

import com.example.lgauthservice.auth.domain.entities.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType;
    private UserLoginData user;
}
