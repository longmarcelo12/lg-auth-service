package com.example.lgauthservice.shared.infrastructure.web.response;

import com.example.lgauthservice.shared.domain.exception.MessageCode;
import com.example.lgauthservice.shared.utils.MessageUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    private final MessageUtils messageUtils;

    public ResponseFactory(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    public <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(buildApiResponse(HttpStatus.OK, data, MessageCode.SUCCESS));
    }

    public <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, String messageCode, T data) {
        return ResponseEntity.status(status).body(buildApiResponse(status, data, messageCode));
    }

    public <T> ApiResponse<T> buildApiResponse(HttpStatus status, T data, String messageCode, Object... params) {
        return ApiResponse.<T>builder()
                .statusCode(status.value())
                .message(messageUtils.getMessage(messageCode, params))
                .data(data)
                .code(messageCode)
                .build();
    }

    public <T> ApiResponse<T> buildApiResponse(HttpStatus status, T data, String message, String code) {
        return ApiResponse.<T>builder()
                .statusCode(status.value())
                .message(message)
                .data(data)
                .code(code)
                .build();
    }

    public ResponseEntity<byte[]> downloadFile(byte[] bytes, String fileName) {
        ContentDisposition disposition = ContentDisposition
                .attachment()
                .filename(fileName)
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(bytes);
    }
}