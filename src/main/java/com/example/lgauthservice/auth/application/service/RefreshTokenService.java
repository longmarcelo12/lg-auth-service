package com.example.lgauthservice.auth.application.service;

import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.presentation.models.request.RefreshTokenRequest;
import com.example.lgauthservice.auth.presentation.models.response.RefreshTokenResponse;

public interface RefreshTokenService {
    String createRefreshToken(User user);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
