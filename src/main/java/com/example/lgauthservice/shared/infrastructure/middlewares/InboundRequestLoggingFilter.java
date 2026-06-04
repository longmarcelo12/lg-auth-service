//package com.example.lgauthservice.shared.infrastructure.middlewares;
//
//import com.example.lgauthservice.shared.constants.Constants;
//import com.example.lgauthservice.shared.enums.LogLevel;
//import com.example.lgauthservice.shared.infrastructure.config.AppConfig;
//import com.example.lgauthservice.shared.infrastructure.kafka.producer.KafkaMessageProducer;
//import com.example.lgauthservice.shared.infrastructure.middlewares.support.HttpUtils;
//import com.example.lgauthservice.shared.presentation.kafka.dtos.LogMessage;
//import com.example.lgauthservice.shared.presentation.kafka.dtos.Metadata;
//import com.example.lgauthservice.shared.utils.StringUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Set;
//import java.util.UUID;
//
//import static com.example.lgauthservice.shared.constants.Constants.*;
//
//@Component
//@Order(SecurityProperties.BASIC_AUTH_ORDER + 2)
//@RequiredArgsConstructor
//public class InboundRequestLoggingFilter extends OncePerRequestFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(InboundRequestLoggingFilter.class);
//
//    private final KafkaMessageProducer kafkaMessageProducer;
//    private final ObjectMapper objectMapper;
//
//    @Value("${kafka.topics.logging-topic:request-log-topic}")
//    private String loggingTopic;
//    @Value("${logging.exclude-body-paths:}")
//    private Set<String> excludeBodyPaths;
//    @Value("${logging.exclude-paths:}")
//    private Set<String> excludePaths;
//    @Value("${logging.log-inbound:true}")
//    private boolean logInboundInfo;
//    @Value("${logging.exclude-headers:}")
//    private static final String PING_PATH = "/ping";
//
//    @PostConstruct
//    public void init() {
//        HttpUtils.init(excludePaths, excludeBodyPaths);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String uri = request.getRequestURI();
//        boolean isStaticPing = uri != null && uri.endsWith(PING_PATH);
//        if (isStaticPing) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        long startTime = System.currentTimeMillis();
//        String requestTime = LocalDateTime.now()
//                .format(DateTimeFormatter.ofPattern(Constants.SHORT_DATETIME_FORMAT_STRING));
//        String requestId = HttpUtils.coalesce(request.getHeader(Constants.Headers.X_REQUEST_ID), request.getHeader(Constants.Headers.X_REQUEST_ID), UUID.randomUUID().toString());
//        String transId = HttpUtils.coalesce(request.getHeader(Constants.Headers.X_TRANSACTION_ID), request.getHeader(Constants.Headers.X_TRANSACTION_ID), UUID.randomUUID().toString());
//        MDC.put(Constants.Headers.X_REQUEST_ID, requestId);
//        MDC.put(Constants.Headers.X_TRANSACTION_ID, transId);
//        MDC.put(Constants.Headers.X_REQUEST_TIME, requestTime);
//
//        HttpServletRequest requestToUse = request;
//        HttpServletResponse responseToUse = response;
//        boolean shouldLogBody = HttpUtils.shouldLogBody(request.getContentType(), request.getMethod(), request.getRequestURI());
//
//        if (shouldLogBody) {
//            requestToUse = new ContentCachingRequestWrapper(request, Constants.LOG_BODY_MAX_PAYLOAD_LENGTH);
//        }
//        if (shouldLogBody) {
//            responseToUse = new ContentCachingResponseWrapper(response);
//        }
//
//        Throwable exception = null;
//        try {
//            filterChain.doFilter(requestToUse, responseToUse);
//        } catch (Exception ex) {
//            exception = ex;
//            throw ex;
//        } finally {
//            String logLevel = (exception != null) ? LogLevel.ERROR.code() : HttpUtils.determineLevel(response.getStatus());
//            boolean isExcluded = uri != null && HttpUtils.shouldSkipLogging(request.getMethod(), uri);
//            if ((logInboundInfo && !isExcluded) || !LogLevel.INFO.code().equals(logLevel)) {
//                logRequestResponse(requestToUse, responseToUse, requestId, transId, startTime, logLevel);
//            }
//            MDC.clear();
//            // Copy body về response (phải luôn có)
//            if (responseToUse instanceof ContentCachingResponseWrapper cachingResponse) {
//                cachingResponse.copyBodyToResponse();
//            }
//        }
//    }
//
//    // === Hàm log chung (dùng cho cả thành công và lỗi) ===
//    private void logRequestResponse(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    String requestId,
//                                    String transactionId,
//                                    long startTime,
//                                    String level) {
//
//        ContentCachingRequestWrapper req = request instanceof ContentCachingRequestWrapper
//                ? (ContentCachingRequestWrapper) request : null;
//        ContentCachingResponseWrapper resp = request instanceof ContentCachingRequestWrapper
//                ? (ContentCachingResponseWrapper) response : null;
//
//        Metadata metadata = new Metadata();
//        metadata.setMethod(request.getMethod());
//        metadata.setUrl(HttpUtils.getFullURL(request));
//        metadata.setHeaders(HttpUtils.buildHeadersNode(request));
//        metadata.setDurationMs(System.currentTimeMillis() - startTime);
//
//        if (req != null) {
//            byte[] reqBuf = req.getContentAsByteArray();
//            if (reqBuf.length > 0) {
//                if (reqBuf.length <= Constants.LOG_BODY_MAX_PAYLOAD_LENGTH) {
//                    // Chuyển trực tiếp sang JsonNode
//                    metadata.setBody(HttpUtils.parseAndTruncate(reqBuf));
//                } else {
//                    metadata.setBody(objectMapper.valueToTree("Request body too large: " + reqBuf.length + " bytes"));
//                }
//            }
//        }
//
//        if (resp != null && HttpUtils.shouldLogBody(response.getContentType(), request.getMethod(), request.getRequestURI())) {
//            byte[] respBuf = resp.getContentAsByteArray();
//            if (respBuf.length > 0) {
//                if (respBuf.length <= Constants.LOG_BODY_MAX_PAYLOAD_LENGTH) {
//                    metadata.setResponse(HttpUtils.parseAndTruncate(respBuf));
//                } else {
//                    metadata.setResponse(objectMapper.valueToTree("Response body too large: " + respBuf.length + " bytes"));
//                }
//            }
//        }
//        String userId = MDC.get(USER_ID);
//        LogMessage logMessage = LogMessage.builder()
//                .requestId(requestId)
//                .transactionId(transactionId)
//                .level(level)  // INFO / WARN / ERROR
//                .userId(null != userId ? Long.parseLong(userId) : null)
//                .templatePath(HttpUtils.getTemplatePath(request))
//                .serviceName(AppConfig.SERVICE_NAME)
//                .createdAt(Instant.now().toString())
//                .metadata(metadata)
//                .build();
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(logMessage);
//            log.info("LogMessage inbound logging filter: {}",
//                    objectMapper.writeValueAsString(logMessage));
//            kafkaMessageProducer.sendMessage(loggingTopic, AppConfig.SERVICE_NAME + "_" + StringUtils.getUUID(), jsonMessage);
//        } catch (Exception e) {
//            log.error("Error serializing or sending log message for RequestID: {}", requestId, e);
//        }
//    }
//}
