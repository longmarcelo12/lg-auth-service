package com.example.lgauthservice.auth.presentation.models.response;

import lombok.Data;

@Data
public class UserLoginData {
    private long id;
    private String email;
    private long roleId;
}
