package com.example.lgauthservice.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Base64;
import java.util.Map;

@UtilityClass
public class JwtUtils {

    public Map<String, Object> jwtDecode(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        String payload = parts[1];
        byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
        String json = new String(decodedBytes);

        return new ObjectMapper().readValue(json, Map.class);
    }
}
