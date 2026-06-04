package com.example.lgauthservice.shared.presentation.mapper;

import com.example.lgauthservice.shared.annotation.Annotations;
import com.example.lgauthservice.shared.utils.Utilities;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@UtilityClass
public class TupleMapper {
    private static final Map<Class<?>, Map<String, Field>> CACHE = new ConcurrentHashMap<>();

    public static <T> List<T> mapTuplesToDtos(List<Tuple> tuples, Class<T> dtoClass) {
        try {
            List<T> result = new ArrayList<>();
            for (Tuple tuple : tuples) {
                result.add(mapTupleToDto(tuple, dtoClass));
            }
            return result.stream().filter(Objects::nonNull).toList();
        } catch (Exception e) {
            log.error("Map Tuples to DTOs failed: {}", ExceptionUtils.getStackTrace(e));
            return new ArrayList<>();
        }
    }

    public static <T> T mapTupleToDto(Tuple tuple, Class<T> dtoClass) {
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();
            Map<String, Field> fieldMap = CACHE.computeIfAbsent(dtoClass, TupleMapper::buildFieldMap);
            for (TupleElement<?> el : tuple.getElements()) {
                String alias = el.getAlias();
                try {
                    if (StringUtils.isBlank(alias)) continue;
                    Field field = Utilities.getOrDefault(fieldMap.get(snakeToCamel(alias)), fieldMap.get(alias));
                    if (ObjectUtils.allNotNull(field)) {
                        Object value = Utilities.returnNullInException(() -> tuple.get(alias));
                        value = convertValue(value, field.getType());
                        field.set(dto, value);
                    }
                } catch (Exception e) {
                    log.error("Set field {} to DTO failed: {}", alias, ExceptionUtils.getStackTrace(e));
                }
            }
            return dto;
        } catch (Exception e) {
            log.error("Map Tuple to DTO failed: {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    private static Map<String, Field> buildFieldMap(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Annotations.ColumnMapping mapping = field.getAnnotation(Annotations.ColumnMapping.class);
            if (mapping != null && StringUtils.isNotBlank(mapping.name())) {
                map.put(mapping.name(), field);
                continue;
            }
            // 1. Map theo field name (camelCase)
            map.put(field.getName(), field);
        }
        return map;
    }

    private static String snakeToCamel(String value) {
        StringBuilder result = new StringBuilder();
        boolean upperNext = false;

        for (char c : value.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else {
                result.append(upperNext ? Character.toUpperCase(c) : c);
                upperNext = false;
            }
        }
        return result.toString();
    }

    private Object convertValue(Object value, Class<?> targetType) {
        try {
            if (value == null) return null; // Dùng check null cơ bản cho nhanh

            if (targetType.isEnum()) {
                return convertEnum(value, targetType);
            }

            if (value instanceof Number num) {
                if (targetType == Long.class) return num.longValue();
                if (targetType == Integer.class) return num.intValue();
                if (targetType == Double.class) return num.doubleValue();
                if (targetType == Float.class) return num.floatValue();
                if (targetType == Short.class) return num.shortValue();
                if (targetType == BigDecimal.class)
                    return (value instanceof BigDecimal b) ? b : BigDecimal.valueOf(num.doubleValue());
                if (targetType == BigInteger.class)
                    return (value instanceof BigInteger b) ? b : BigInteger.valueOf(num.longValue());
            }

            // --- String ---
            if (targetType == String.class) return value.toString();

            // --- Boolean ---
            if (targetType == Boolean.class) {
                if (value instanceof Number n) return n.intValue() != 0;
                if (value instanceof String s) return Boolean.parseBoolean(s);
                return value;
            }

            if (targetType == LocalDateTime.class) {
                if (value instanceof Timestamp ts) return ts.toLocalDateTime();
                if (value instanceof java.sql.Date sd) return sd.toLocalDate().atStartOfDay();
                if (value instanceof Date d) return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }

            else if (targetType == LocalDate.class) {
                if (value instanceof java.sql.Date sd) return sd.toLocalDate();
                if (value instanceof Date d) return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (value instanceof Timestamp ts) return ts.toLocalDateTime().toLocalDate();
            }

            else if (targetType == LocalTime.class) {
                if (value instanceof Time t) return t.toLocalTime();
                if (value instanceof Timestamp ts) return ts.toLocalDateTime().toLocalTime();
            }

            else if (targetType == Instant.class) {
                if (value instanceof Timestamp ts) return ts.toInstant();
                if (value instanceof java.sql.Date sd) return sd.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
                if (value instanceof Date d) return d.toInstant();
            }

            // --- UUID ---
            if (targetType == UUID.class) return UUID.fromString(value.toString());

            return targetType.cast(value);
        } catch (Exception e) {
            log.error("Mapping failed for target type {}: {}", targetType.getSimpleName(), e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Object convertEnum(Object value, Class<?> targetType) {

        if (!targetType.isEnum() || value == null) {
            return value;
        }

        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) targetType;

        // 1️⃣ Try static fromValue method
        for (Method method : enumClass.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())
                    && method.getName().equals("fromValue")
                    && method.getParameterCount() == 1) {
                try {
                    return method.invoke(null, value);
                } catch (Exception ignored) {
                }
            }
        }

        // 2️⃣ Match by field "value"
        for (Enum<?> constant : enumClass.getEnumConstants()) {
            try {
                Field valueField = enumClass.getDeclaredField("value");
                valueField.setAccessible(true);
                Object enumValue = valueField.get(constant);

                if (enumValue != null && enumValue.toString().equals(value.toString())) {
                    return constant;
                }
            } catch (Exception ignored) {
            }
        }

        // 3️⃣ Fallback by name
        return Enum.valueOf((Class) enumClass, value.toString().toUpperCase());
    }
}
