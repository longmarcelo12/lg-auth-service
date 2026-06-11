package com.example.lgauthservice.shared.domain.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class UnauthorizedException extends BusinessException {
    private String type;
    public UnauthorizedException(String message, Object data) {
        super(
                HttpStatus.UNAUTHORIZED,
                message,
                data
        );
        this.type = "Login";
    }

    public UnauthorizedException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                message,
                null
        );
        this.type = "Login";
    }
}