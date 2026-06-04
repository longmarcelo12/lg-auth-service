package com.example.lgauthservice.shared.domain.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {

    public <T> BadRequestException(String messageCode, T data, Object... args) {
        super(
                HttpStatus.BAD_REQUEST,
                messageCode,
                data,
                args
        );
    }

    public BadRequestException(String messageCode, ErrorDetail errorDetail) {
        super(
                HttpStatus.BAD_REQUEST,
                messageCode,
                errorDetail
        );
    }
}
