package com.example.lgauthservice.shared.domain.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BusinessException {
    public InternalServerException(String error, Object... args) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                error,
                null,
                args
        );
    }

    public InternalServerException() {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                MessageCode.INTERNAL_SERVER_ERROR,
                null
        );
    }
}
