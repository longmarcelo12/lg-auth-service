package com.example.lgauthservice.shared.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;

@UtilityClass
@Slf4j
public class TypeConverterUtils {

    public <T> T convert(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        if (type.equals(String.class)) {
            return type.cast(value.toString());
        }
        if (type.equals(Integer.class)) {
            return type.cast(Integer.valueOf(value.toString()));
        }
        if (type.equals(Long.class)) {
            return type.cast(Long.valueOf(value.toString()));
        }
        if (type.equals(Double.class)) {
            return type.cast(Double.valueOf(value.toString()));
        }
        if (type.equals(Boolean.class)) {
            return type.cast(Boolean.valueOf(value.toString()));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Hibernate6Module());
        try {
            if (value instanceof String json) {
                return mapper.readValue(json, type);
            }
            if (value instanceof Map map) {
                return mapper.convertValue(map, type);
            }
            return type.cast(value);
        } catch (Exception e) {
            log.error("Cannot convert value : {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
