package com.example.lgauthservice.auth.presentation.controller;

import com.example.lgauthservice.auth.application.service.AuthService;
import com.example.lgauthservice.auth.application.service.EmailService;
import com.example.lgauthservice.auth.presentation.models.request.LoginRequest;
import com.example.lgauthservice.auth.presentation.models.request.RegisterRequest;
import com.example.lgauthservice.auth.presentation.models.response.LoginResponse;
import com.example.lgauthservice.auth.presentation.models.response.RegisterResponse;
import com.example.lgauthservice.auth.presentation.models.response.VerifyTokenResponse;
import com.example.lgauthservice.shared.infrastructure.web.response.ApiResponse;
import com.example.lgauthservice.shared.infrastructure.web.response.ResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;
    private final ResponseFactory responseFactory;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = authService.registerUser(registerRequest);
        return responseFactory.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return responseFactory.ok(response);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<ApiResponse<VerifyTokenResponse>> verifyToken(@Valid @RequestParam("token") String token) {
        return responseFactory.ok(emailService.verifyToken(token));
    }


}
