package com.example.lgauthservice.auth.application.service;

import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.presentation.models.response.GenerateTokenResponse;

public interface JwtService {
    String generateAccessToken(User user);
}
