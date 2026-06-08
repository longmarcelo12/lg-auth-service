package com.example.lgauthservice.auth.presentation.models.request;

import com.example.lgauthservice.shared.annotation.Annotations;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "email is required")
    @Annotations.Email
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank
    private String passwordConfirmation;
}
