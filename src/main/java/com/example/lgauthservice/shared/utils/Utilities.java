package com.example.lgauthservice.shared.utils;

import com.example.lgauthservice.shared.constants.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class Utilities {

    private final static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .registerModule(new JavaTimeModule())
            .registerModule(new Hibernate6Module());

    public <T> T copyProperties(Object source, Class<T> clazz) {
        try {
            if (source instanceof String json) {
                return mapper.readValue(json, clazz);
            }
            String json = mapper.writeValueAsString(source);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Utilities copy properties: {} ", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public <T> List<T> copyProperties(List<?> source, Class<T> clazz) {
        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return mapper.readValue(mapper.writeValueAsString(source), type);
        } catch (Exception e) {
            log.error("Utilities copy properties list: {} ", ExceptionUtils.getStackTrace(e));
            return new ArrayList<>();
        }
    }

    public <T, K> K updateProperties(T source, K target) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();
        copyData(source, target, sourceFields, targetFields);
        try {
            Class<?> superClass = source.getClass().getSuperclass();
            sourceFields = superClass.getDeclaredFields();
            copyData(source, target, sourceFields, targetFields);
        } catch (Exception e) {
            log.error("Utilities update properties list: {} ", ExceptionUtils.getStackTrace(e));
        }
        return target;
    }

    private <K, T> void copyData(T source, K target, Field[] sourceFields, Field[] targetFields) {
        for (Field sourceField : sourceFields) {
            try {
                sourceField.setAccessible(true);
                for (Field targetField : targetFields) {
                    try {
                        targetField.setAccessible(true);
                        if (Objects.nonNull(sourceField.get(source)) && sourceField.getName().equals(targetField.getName())
                                && sourceField.getType().equals(targetField.getType())) {
                            targetField.set(target, sourceField.get(source));
                            break;
                        }
                    } catch (IllegalAccessException e) {
                    } finally {
                        targetField.setAccessible(false);
                    }
                }
            } finally {
                sourceField.setAccessible(false);
            }
        }
    }

    public <T> T returnNullInException(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            return null;
        }
    }

    public <T> T getOrDefault(T value, T defaultValue) {
        return ObjectUtils.allNull(value) ? defaultValue : value;
    }

    public static String getRequestTimeValue() {
        return (MDC.get(Constants.Headers.X_REQUEST_TIME) != null) ? MDC.get(Constants.Headers.X_REQUEST_TIME).toString() : LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Constants.SHORT_DATETIME_FORMAT_STRING));
    }

    public static boolean isNullOrEmpty(Object o) {
        return o == null || o.toString().trim().isEmpty();
    }

    public <T> List<T> convertStringToList(String source, Class<T> clazz) {
        if (StringUtils.isBlank(source)) return new ArrayList<>();
        try {
            return mapper.readValue(source,
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            log.error("Utilities convertStringToList: {} ", ExceptionUtils.getStackTrace(e));
            return new ArrayList<>();
        }
    }
}
