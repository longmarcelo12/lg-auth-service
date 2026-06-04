package com.example.lgauthservice.shared.utils.trim_text;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

@JsonComponent
class TrimTextJsonBody extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        return parser.hasToken(VALUE_STRING) ? StringUtils.stripToEmpty(parser.getText().trim()) : null;
    }
}