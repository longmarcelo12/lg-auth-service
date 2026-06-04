package com.example.lgauthservice.shared.utils.converter;

import com.example.lgauthservice.shared.utils.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class VnPhoneNumberDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return StringUtils.formatVnPhoneNumber(p.getValueAsString(), null);
    }
}
