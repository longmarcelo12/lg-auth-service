package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.RefreshTokenService;
import com.example.lgauthservice.auth.domain.entities.RefreshToken;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.infrastructure.config.JwtProperties;
import com.example.lgauthservice.auth.presentation.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(token)
                .revoked(false)
                .expiresAt(Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpiration()))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

}
