package com.example.lgauthservice.auth.application.service;

import com.example.lgauthservice.auth.presentation.models.response.VerifyTokenResponse;

public interface EmailService {
    void sendVerificationEmail(String email, String token);

    VerifyTokenResponse verifyToken(String token);
}
