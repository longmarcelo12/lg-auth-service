package com.example.lgauthservice.shared.domain.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageCode {
    public final String SUCCESS = "SUCCESS";
    public final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
    public final String BAD_REQUEST = "BAD_REQUEST";
    public final String NOT_FOUND = "NOT_FOUND";
    public final String FORBIDDEN = "FORBIDDEN";

    public final String ERR_SHARED_0101 = "ERR_SHARED_0101";
    public final String ERR_SHARED_0102 = "ERR_SHARED_0102";
}
