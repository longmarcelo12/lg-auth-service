package com.example.lgauthservice.shared.utils.converter;

import com.example.lgauthservice.shared.constants.Constants;
import com.example.lgauthservice.shared.utils.DateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DateTimeJsonDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        DateTimeFormatter fmt;
        Instant result;
        try {
            fmt = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_YYYYMMDD_HYPHEN).withZone(DateUtils.SYSTEM_DEFAULT_ZONE);
            result = Instant.from(fmt.parse(p.getText()));
        } catch (Exception e) {
            try {
                fmt = DateTimeFormatter.ofPattern(Constants.FULL_DATETIME_FORMAT_WITH_ZONE).withZone(DateUtils.SYSTEM_DEFAULT_ZONE);
                result = Instant.from(fmt.parse(p.getText()));
            } catch (Exception ex) {
                fmt = DateTimeFormatter.ofPattern(Constants.SHORT_DATE_FORMAT_YYYYMMDD_HYPHEN).withZone(DateUtils.SYSTEM_DEFAULT_ZONE);
                result = Instant.from(fmt.parse(p.getText()));
            }
        }
        return result;
    }
}
