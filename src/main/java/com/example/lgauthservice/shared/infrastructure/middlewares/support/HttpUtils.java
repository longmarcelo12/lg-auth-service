//package com.example.lgauthservice.shared.infrastructure.middlewares.support;
//
//import com.example.lgauthservice.shared.constants.Constants;
//import com.example.lgauthservice.shared.enums.LogLevel;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonToken;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.experimental.UtilityClass;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.MDC;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.servlet.HandlerMapping;
//
//import java.util.*;
//
//import static com.example.lgauthservice.shared.constants.Constants.*;
//
//@UtilityClass
//public class HttpUtils {
//    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
//    private static final List<ExcludePattern> patternPaths = new ArrayList<>();
//    private static final List<ExcludePattern> patternBodyPaths = new ArrayList<>();
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//    private static final int MAX_FIELD_LEN = 1000;
//
//    public static void init(Set<String> rawExcludePaths, Set<String> rawExcludeBodyPaths) {
//        if (!rawExcludePaths.isEmpty()) {
//            rawExcludePaths.forEach(path -> patternPaths.add(new ExcludePattern(path)));
//        }
//        if (!rawExcludeBodyPaths.isEmpty()) {
//            rawExcludeBodyPaths.forEach(path -> patternBodyPaths.add(new ExcludePattern(path)));
//        }
//    }
//
//    public String coalesce(String... values) {
//        for (String v : values) {
//            if (v != null && !v.isBlank()) return v.trim();
//        }
//        return UUID.randomUUID().toString();
//    }
//
//    public boolean isSensitiveHeader(String name) {
//        String lower = name.toLowerCase();
//        return lower.contains(Constants.Headers.AUTHORIZATION.toLowerCase())
//                || lower.contains(Constants.Headers.COOKIE.toLowerCase())
//                || lower.contains(Constants.Headers.SET_COOKIE.toLowerCase())
//                || lower.contains(Constants.Headers.X_API_KEY.toLowerCase())
//                || lower.contains(Constants.Headers.PROXY_AUTHORIZATION.toLowerCase())
//                || lower.equalsIgnoreCase(Constants.Headers.MSB_API_KEY)
//                || lower.equalsIgnoreCase(Constants.CUSTOM);
//    }
//
//    public String truncated(String input) {
//        return StringUtils.abbreviate(input, 1000);
//    }
//
//    public boolean isMultipart(String contentType) {
//        return StringUtils.isBlank(contentType) && contentType.toLowerCase().startsWith("multipart/");
//    }
//
//    public boolean isTextBasedContentType(String contentType) {
//        if (StringUtils.isBlank(contentType)) return true;
//        String lower = contentType.toLowerCase();
//        return lower.contains("json") || lower.contains("xml") || lower.contains("text/") || lower.contains("x-www-form-urlencoded");
//    }
//
//    public boolean shouldLogBody(String contentType, String method, String path) {
//        return isTextBasedContentType(contentType) && !shouldSkipLoggingBody(method, path);
//    }
//
//    public boolean shouldSkipLogging(String method, String uri) {
//        if (uri == null) return false;
//        return patternPaths.stream().anyMatch(p -> p.matches(uri, method));
//    }
//
//    private boolean shouldSkipLoggingBody(String method, String uri) {
//        if (uri == null) return false;
//        return patternBodyPaths.stream().anyMatch(p -> p.matches(uri, method));
//    }
//
//    public String determineLevel(int status) {
//        if (status >= 500) return LogLevel.ERROR.code();
//        if (status >= 400) return LogLevel.WARN.code();
//        String logLevel = Optional.ofNullable(MDC.get(KEY_GLOBAL_EXCEPTION_HANDLER_STATUS_CODE))
//                .map(Object::toString)
//                .orElse("0");
//        try {
//            int statusCode = Integer.parseInt(logLevel);
//            if (statusCode >= 500) {
//                return LogLevel.ERROR.code();
//            }
//            if (statusCode >= 400) {
//                return LogLevel.WARN.code();
//            }
//        } catch (NumberFormatException e) {
//            return LogLevel.ERROR.code();
//        }
//        return LogLevel.INFO.code();
//    }
//
//    private class ExcludePattern {
//        private String path;
//        private String method;
//        private boolean hasWildcard;
//        private boolean hasMethod;
//
//        public ExcludePattern(String raw) {
//            if (raw.contains(":")) {
//                String[] parts = raw.split(":");
//                this.path = parts[0].trim();
//                this.method = parts[1].trim();
//                this.hasMethod = true;
//            } else {
//                this.path = raw;
//            }
//            this.hasWildcard = this.path.contains("*");
//        }
//
//        public boolean matches(String uri, String currentMethod) {
//            if (this.hasMethod && !this.method.equalsIgnoreCase(currentMethod)) {
//                return false;
//            }
//            if (hasWildcard) {
//                return pathMatcher.match(this.path, uri);
//            }
//            return path.equals(uri);
//        }
//    }
//
//    public static JsonNode parseAndTruncate(byte[] buf) {
//        if (buf == null || buf.length == 0) return null;
//        try (JsonParser parser = objectMapper.getFactory().createParser(buf)) {
//            ObjectNode root = objectMapper.createObjectNode();
//
//            while (parser.nextToken() != null) {
//                String fieldName = parser.currentName();
//                if (fieldName != null) {
//                    JsonToken token = parser.nextToken();
//                    if (token == JsonToken.VALUE_STRING) {
//                        String value = parser.getText();
//                        if (value.length() > MAX_FIELD_LEN) {
//                            root.put(fieldName, "[TRUNCATED: " + value.length() + " chars]");
//                        } else {
//                            root.put(fieldName, value);
//                        }
//                    } else {
//                        root.set(fieldName, objectMapper.readTree(parser));
//                    }
//                }
//            }
//            return root;
//        } catch (Exception e) {
//            return objectMapper.createObjectNode().put("raw", "Parse error");
//        }
//    }
//
//    public static ObjectNode buildHeadersNode(HttpServletRequest request) {
//        ObjectNode headers = objectMapper.createObjectNode();
//        Enumeration<String> names = request.getHeaderNames();
//        while (names.hasMoreElements()) {
//            String name = names.nextElement();
//            if (HttpUtils.isSensitiveHeader(name)) {
//                headers.put(name.toLowerCase(), "*****");
//            } else {
//                headers.put(name.toLowerCase(), request.getHeader(name));
//            }
//        }
//        return headers;
//    }
//
//    public static String getFullURL(HttpServletRequest request) {
//        StringBuilder url = new StringBuilder(request.getRequestURL());
//        if (request.getQueryString() != null) {
//            url.append(QUESTION_MARK).append(request.getQueryString());
//        }
//        return url.toString();
//    }
//
//    public static ObjectNode buildHeadersNode(Map<String, Collection<String>> headers) {
//        ObjectNode response = objectMapper.createObjectNode();
//        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
//            String name = entry.getKey();
//            Collection<String> values = entry.getValue();
//            String lowerName = name.toLowerCase();
//            if (HttpUtils.isSensitiveHeader(name)) {
//                response.put(lowerName, "*****");
//            } else {
//                if (values.size() == 1) {
//                    response.put(lowerName, values.iterator().next());
//                } else {
//                    ArrayNode arr = objectMapper.createArrayNode();
//                    values.forEach(arr::add);
//                    response.set(lowerName, arr);
//                }
//            }
//        }
//        return response;
//    }
//
//
//    public static String getRequestIdValue() {
//        if (MDC.get(Constants.Headers.X_REQUEST_ID) != null) {
//            return MDC.get(Constants.Headers.X_REQUEST_ID).toString();
//        } else {
//            String requestId = UUID.randomUUID().toString();
//            MDC.put(Constants.Headers.X_REQUEST_ID, requestId);
//            return requestId;
//        }
//    }
//
//    public static String getTransactionIdValue() {
//        if (MDC.get(Constants.Headers.X_REQUEST_ID) != null) {
//            return MDC.get(Constants.Headers.X_TRANSACTION_ID).toString();
//        } else {
//            String requestId = UUID.randomUUID().toString();
//            MDC.put(Constants.Headers.X_TRANSACTION_ID, requestId);
//            return requestId;
//        }
//    }
//
//    public static String getTemplatePath(HttpServletRequest request) {
//        // Ưu tiên PathPattern (Spring 5.3+)
//        Object pathPattern = request.getAttribute(
//                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
//        if (pathPattern != null) {
//            return pathPattern.toString();
//        }
//        // Fallback cho AntPath (cũ hơn)
//        Object antPattern = request.getAttribute(
//                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//
//        if (antPattern != null) {
//            return antPattern.toString();
//        }
//        return null;
//    }
//}
