package com.example.lgauthservice.auth.presentation.models.response;

import com.example.lgauthservice.auth.domain.entities.User;
import lombok.Data;

@Data
public class LoginResponse {
    private User user;
}
