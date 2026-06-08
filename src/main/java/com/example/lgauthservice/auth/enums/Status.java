package com.example.lgauthservice.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.ObjectUtils;

public enum Status {
    PENDING("pending"),
    ACTIVE("active"),
    INACTIVE("inactive");

    private String value;

    Status(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Status fromValue(String value) {
        if(ObjectUtils.allNull(value)) return null;
        for (Status status : Status.values()) {
            if(status.value.equals(value))
                return status;
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return this.value;
    }
}
