package com.example.lgauthservice.auth.application.service;

import com.example.lgauthservice.auth.presentation.models.response.VerifyEmailResponse;

public interface EmailService {
    void sendVerificationEmail(String email, String token);

    VerifyEmailResponse verifyToken(String token);
}
