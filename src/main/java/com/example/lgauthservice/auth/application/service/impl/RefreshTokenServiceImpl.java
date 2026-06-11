package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.JwtService;
import com.example.lgauthservice.auth.application.service.RefreshTokenService;
import com.example.lgauthservice.auth.domain.entities.RefreshToken;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.infrastructure.config.JwtProperties;
import com.example.lgauthservice.auth.presentation.models.request.RefreshTokenRequest;
import com.example.lgauthservice.auth.presentation.models.response.RefreshTokenResponse;
import com.example.lgauthservice.auth.presentation.repository.RefreshTokenRepository;
import com.example.lgauthservice.auth.presentation.repository.UserRepository;
import com.example.lgauthservice.shared.domain.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(token)
                .revoked(false)
                .expiredAt(Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpiration()))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        // lấy và kiểm tra token hợp lệ
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Token not found", null));

        if(Boolean.TRUE.equals(refreshToken.isRevoked())) {
            throw new BadRequestException("Token revoked", null, null);
        }

        if(refreshToken.getExpiredAt().isBefore(Instant.now())) {
            throw new BadRequestException("Token expired", null, null);
        }

        // lấy thông tin user
        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new BadRequestException("User not found", null));

        // lấy access token mới
        String accessToken = jwtService.generateAccessToken(user);

        // cập nhật lại refresh token cũ
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // lấy và cập nhật refresh token mới
        String newRefreshToken = createRefreshToken(user);

        return RefreshTokenResponse.builder().accessToken(accessToken).refreshToken(newRefreshToken).build();
    }
}
