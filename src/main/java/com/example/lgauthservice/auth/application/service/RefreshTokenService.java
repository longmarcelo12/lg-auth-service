package com.example.lgauthservice.auth.application.service;

import com.example.lgauthservice.auth.domain.entities.User;

public interface RefreshTokenService {
    String createRefreshToken(User user);
}
