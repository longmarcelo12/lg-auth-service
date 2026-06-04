package com.example.lgauthservice.shared.enums;

public enum LogLevel {
    TRACE("TRACE"),
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR"),
    FATAL("FATAL");
    private final String code;

    LogLevel(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
