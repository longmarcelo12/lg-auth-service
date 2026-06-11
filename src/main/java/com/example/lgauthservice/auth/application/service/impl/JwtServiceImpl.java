package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.JwtService;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.infrastructure.config.JwtProperties;
import com.example.lgauthservice.auth.presentation.models.response.GenerateTokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
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

    public String extractUserId(String token) {
        Claims claims = claimsFromToken(token);
        log.info("claims: " + claims);
        return claims.getSubject();
    }

    public SecretKey getSecretKey() {
        log.info("getSecretKey: {}", jwtProperties.getSecretKey());
        byte[] encodedKey = Decoders.BASE64.decode(jwtProperties.getSecretKey());

        return Keys.hmacShaKeyFor(encodedKey);
    }

    public boolean isValidToken(String token) {
        try {
            claimsFromToken(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public Claims claimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(
                        getSecretKey()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey()
                        .getBytes(
                                StandardCharsets.UTF_8
                        )
        );
    }
}
