package com.example.lgauthservice.shared.domain.exception;

import lombok.Getter;

@Getter
public class FeignClientException extends RuntimeException {
    private final int statusCode;

    public FeignClientException(String message) {
        super(message);
        this.statusCode = 500; // default
    }

    public FeignClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
