package com.example.lgauthservice.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.ObjectUtils;

public enum Provider {
    LOCAL("local"),
    GOOGLE("google"),
    GITHUB("github");

    private String value;

    Provider(String value) {
        this.value = value;
    }

    @JsonCreator
    public Provider fromValue(String value) {
        if(ObjectUtils.allNull(value)) return null;
        for(Provider provider : Provider.values()) {
            if(value.equals(provider.value)) return provider;
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return value;
    }
}
