package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.JwtService;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.infrastructure.config.JwtProperties;
import com.example.lgauthservice.auth.presentation.models.response.GenerateTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperties jwtProperties;

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration() * 1000);

        return Jwts.builder().subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("roleId", user.getRoleId())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    public SecretKey getSecretKey() {
        byte[] encodedKey = Decoders.BASE64.decode(jwtProperties.getSecretKey());

        return Keys.hmacShaKeyFor(encodedKey);
    }
}
