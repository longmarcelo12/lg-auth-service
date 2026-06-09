package com.example.lgauthservice.auth.application.service;

import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.presentation.models.request.LoginRequest;
import com.example.lgauthservice.auth.presentation.models.request.RegisterRequest;
import com.example.lgauthservice.auth.presentation.models.response.LoginResponse;
import com.example.lgauthservice.auth.presentation.models.response.RegisterResponse;
import com.example.lgauthservice.auth.presentation.models.response.VerifyTokenResponse;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
