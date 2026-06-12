package com.example.lgauthservice.auth.presentation.models.request;

import com.example.lgauthservice.shared.annotation.Annotations;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPasswordRequest {
    @Annotations.Email
    @NotBlank(message = "Email is not empty")
    private String email;
}
