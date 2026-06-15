package com.example.lgauthservice.auth.presentation.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "token is not empty")
    private String token;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirmation;
}

