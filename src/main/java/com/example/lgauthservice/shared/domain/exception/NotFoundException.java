package com.example.lgauthservice.shared.domain.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String error, Object... args) {
        super(
                HttpStatus.NOT_FOUND,
                error,
                null,
                args
        );
    }
}
