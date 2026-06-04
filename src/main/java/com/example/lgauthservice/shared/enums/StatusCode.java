package com.example.lgauthservice.shared.enums;

public enum StatusCode {
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400);

    private final int code;
    StatusCode(int code) { this.code = code; }

    public int getCode() {
        return code;
    }
}
