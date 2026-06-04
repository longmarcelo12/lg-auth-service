package com.example.lgauthservice.shared.infrastructure.web.support;

import com.example.lgauthservice.shared.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public final class ClientIpResolver {

    private ClientIpResolver() {}

    public static String resolve(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "CF-Connecting-IP",
                "True-Client-IP"
        };
        for (String header : headers) {
            String value = request.getHeader(header);
            if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
                return value.split(Constants.COMMA)[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}