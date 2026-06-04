package com.example.lgauthservice.shared.domain.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private int statusCode;
    private String messageCode;
    private String requestId;
    private Object data;
    private Object[] args;

    public BusinessException() {
        super();
    }

    public BusinessException(HttpStatus status, String messageCode, Object data, Object... args) {
        super();
        this.statusCode = status.value();
        this.messageCode = messageCode;
        this.data = data;
        this.args = args;
    }
}
