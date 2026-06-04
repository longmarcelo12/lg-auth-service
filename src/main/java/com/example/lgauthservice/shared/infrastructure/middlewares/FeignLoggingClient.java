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
//import com.example.lgauthservice.shared.utils.Utilities;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import feign.Client;
//import feign.Request;
//import feign.Response;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.hibernate.validator.internal.util.stereotypes.Lazy;
//import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Collections;
//
//import static com.example.lgauthservice.shared.constants.Constants.*;
//import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class FeignLoggingClient implements Client {
//
//    private final Client delegate;
//    private final @Lazy KafkaMessageProducer logProducer;
//    private final ObjectMapper objectMapper;
//    @Value("${kafka.topics.logging-topic}")
//    private String loggingTopic;
//    @Value("${internal-token.token}")
//    private String internalToken;
//    @Value("${logging.log-feign:true}")
//    private boolean logFeignInfo;
//
//    @Override
//    public Response execute(Request request, Request.Options options) throws IOException {
//        long startTime = System.currentTimeMillis();
//        String requestTime = HttpUtils.coalesce(
//                request.headers().getOrDefault(Constants.Headers.X_REQUEST_TIME, Collections.emptyList()).stream().findFirst().orElse(null),
//                LocalDateTime.now()
//                        .format(DateTimeFormatter.ofPattern(Constants.SHORT_DATETIME_FORMAT_STRING))
//        );
//        String httpMethod = null != request.httpMethod() ? request.httpMethod().name() : StringUtils.EMPTY;
//        boolean shouldLogBody = HttpUtils.shouldLogBody(request.headers().getOrDefault(CONTENT_TYPE, Collections.emptyList())
//                .stream().findFirst().orElse(StringUtils.EMPTY), httpMethod, request.url());
//        String requestId = HttpUtils.getRequestIdValue();
//        String transId = HttpUtils.getTransactionIdValue();
//        MDC.put(Constants.Headers.X_REQUEST_TIME, requestTime);
//        MDC.put(Constants.Headers.TOKEN, internalToken);
////        log.info("Executing Feign request. RequestId: {}, URL: {}, Client instance: {}", requestId, request.url(), this);
//
//        LogLevel logLevel = LogLevel.INFO;
//        Metadata metadata = new Metadata();
//        metadata.setUrl(request.url());
//        metadata.setMethod(httpMethod);
//        metadata.setHeaders(HttpUtils.buildHeadersNode(request.headers()));
//        if (shouldLogBody && null != request.body()) {
//            long bodyLength = request.body().length;
//            if (bodyLength <= Constants.LOG_BODY_MAX_PAYLOAD_LENGTH) {
//                metadata.setBody(HttpUtils.parseAndTruncate(request.body()));
//            } else {
//                objectMapper.createObjectNode().put("raw", "Request body can't log: " + bodyLength + " bytes");
//            }
//        }
//        try {
//            Response response = delegate.execute(request, options);
//            Integer responseStatus = Utilities.returnNullInException(response::status);
//            if (ObjectUtils.allNull(responseStatus) || responseStatus >= 400) {
//                logLevel = LogLevel.ERROR;
//            }
//            byte[] respData = null;
//            if (shouldLogBody && response.body() != null) {
//                long contentLength = response.body().length() != null ? response.body().length() : 0;
//                if (contentLength <= Constants.LOG_BODY_MAX_PAYLOAD_LENGTH) {
//                    respData = response.body().asInputStream().readAllBytes();
//                    if (respData.length > 0) {
//                        metadata.setResponse(HttpUtils.parseAndTruncate(respData));
//                    }
//                } else {
//                    objectMapper.createObjectNode().put("raw", "Response body too large to log: " + contentLength + " bytes");
//                }
//            }
//
//            if (respData != null) {
//                response = response.toBuilder().body(respData).build();
//            }
//            return response;
//        } catch (IOException | RuntimeException ex) {
//            logLevel = LogLevel.ERROR;
//            metadata.setResponse(objectMapper.createObjectNode().put("error", HttpUtils.truncated(ExceptionUtils.getMessage(ex))));
//            throw ex;
//        } finally {
//            metadata.setDurationMs(System.currentTimeMillis() - startTime);
//            LogMessage logMessage = new LogMessage();
//            logMessage.setRequestId(requestId);
//            logMessage.setTransactionId(transId);
//            logMessage.setServiceName(AppConfig.SERVICE_NAME);
//            String userId = MDC.get(USER_ID);
//            logMessage.setUserId(null != userId ? Long.parseLong(userId) : null);
//            logMessage.setTemplatePath(request.requestTemplate().path());
//            logMessage.setLevel(logLevel.code());
//            logMessage.setMetadata(metadata);
//            logMessage.setCreatedAt(LocalDateTime.now().toString());
//            logMessage.set__v(0);
//
//            String jsonMessage = objectMapper.writeValueAsString(logMessage);
//            if (logFeignInfo) {
//                logProducer.sendMessage(loggingTopic, AppConfig.SERVICE_NAME + "_" + StringUtils.getUUID(), jsonMessage);
//            }
//            log.info("Feign execute: requestId={}, url={}, Ms={}, instance={}, Message={}",
//                    requestId, request.url(), System.currentTimeMillis() - startTime, this, jsonMessage);
//        }
//    }
//
//}
