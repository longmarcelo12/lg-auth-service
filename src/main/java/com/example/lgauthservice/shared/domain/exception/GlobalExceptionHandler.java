package com.example.lgauthservice.shared.domain.exception;

import com.example.lgauthservice.shared.infrastructure.web.response.ApiResponse;
import com.example.lgauthservice.shared.infrastructure.web.response.ResponseFactory;
import com.example.lgauthservice.shared.utils.MessageUtils;
import com.example.lgauthservice.shared.utils.StringUtils;
import com.example.lgauthservice.shared.utils.Utilities;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.lgauthservice.shared.constants.Constants.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ResponseFactory responseFactory;
    private final MessageUtils messageUtils;

    private static final Logger logbackLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(ResponseFactory responseFactory, MessageUtils messageUtils) {
        this.responseFactory = responseFactory;
        this.messageUtils = messageUtils;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception exception) {
        logbackLogger.error("Exception: {}", ExceptionUtils.getStackTrace(exception));
        ApiResponse<Object> response = responseFactory.buildApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, MessageCode.INTERNAL_SERVER_ERROR);
        return builderResponseEntity(response);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoSuchFileException(NoSuchFileException exception) {
        logbackLogger.error("NoSuchFileException: {}", ExceptionUtils.getStackTrace(exception));
        ApiResponse<Object> response = responseFactory.buildApiResponse(HttpStatus.NOT_FOUND, null, MessageCode.NOT_FOUND);
        return builderResponseEntity(response);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingRequestHeaderException(org.springframework.web.bind.MissingRequestHeaderException ex) {
        logbackLogger.error("MissingRequestHeaderException: {}", ex.getMessage());
        String errorMessage = String.format("Required header '%s' is missing", ex.getHeaderName());
        ApiResponse<Object> response = responseFactory.buildApiResponse(
                HttpStatus.BAD_REQUEST,
                null,
                errorMessage,
                HttpStatus.BAD_REQUEST.name()
        );
        return builderResponseEntity(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        HttpStatus httpStatus = Utilities.getOrDefault(HttpStatus.resolve(ex.getStatusCode()), HttpStatus.BAD_REQUEST);
        ApiResponse<Object> response = responseFactory.buildApiResponse(httpStatus, ex.getData(), ex.getMessageCode(), ex.getArgs());
        return builderResponseEntity(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return builderResponseEntity(handleArgumentNotValidException(exception));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fullPath = violation.getPropertyPath().toString();
            String field = StringUtils.extractFieldName(fullPath);
            fieldErrors.put(field, messageUtils.getMessage(violation.getMessage(), field));
        }
        ApiResponse<Object> response = responseFactory.buildApiResponse(HttpStatus.BAD_REQUEST, fieldErrors, MessageCode.BAD_REQUEST);
        return builderResponseEntity(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        logbackLogger.error("MaxUploadSizeExceededException: File too large!");
        ApiResponse<Object> response = responseFactory.buildApiResponse(HttpStatus.EXPECTATION_FAILED, null, MessageCode.BAD_REQUEST);
        return builderResponseEntity(response);
    }

    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ApiResponse<Object>> handleFeignClientException(FeignClientException ex) {
        logbackLogger.error("FeignClientException: {}", ex.getMessage());
        HttpStatus httpStatus = Utilities.getOrDefault(HttpStatus.resolve(ex.getStatusCode()), HttpStatus.BAD_GATEWAY);
        ApiResponse<Object> response = responseFactory.buildApiResponse(
                httpStatus,
                null,
                "Error from external service: " + ex.getMessage(),
                HttpStatus.BAD_GATEWAY.name()
        );
        return builderResponseEntity(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException ex) {
        ApiResponse<Object> response = responseFactory.buildApiResponse(
                HttpStatus.NOT_FOUND,
                null,
                "API not found: /" + ex.getResourcePath(),
                HttpStatus.NOT_FOUND.name()
        );
        return builderResponseEntity(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ApiResponse<Object> response = responseFactory.buildApiResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                null,
                String.format("Method %s is not supported", ex.getMethod()),
                HttpStatus.METHOD_NOT_ALLOWED.name()
        );
        return builderResponseEntity(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logbackLogger.error("HttpMessageNotReadableException: {}", ex.getMessage());
        ApiResponse<Object> response = responseFactory.buildApiResponse(
                HttpStatus.BAD_REQUEST,
                null,
                "The request body is invalid or the JSON structure is malformed.",
                HttpStatus.BAD_GATEWAY.name()
        );
        return builderResponseEntity(response);
    }


    //--------------------------//
    private ApiResponse<Object> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        Map<String, String> fieldErrors = new HashMap<>();
        for (ObjectError objectError : objectErrors) {
            if (objectError instanceof FieldError fieldError) {
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                String field = String.valueOf(Utilities.returnNullInException(() -> objectError.getArguments()[1]));
                fieldErrors.put(field, objectError.getDefaultMessage());
            }
        }
        return responseFactory.buildApiResponse(HttpStatus.BAD_REQUEST, fieldErrors, MessageCode.BAD_REQUEST);
    }

    private ResponseEntity<ApiResponse<Object>> builderResponseEntity(ApiResponse<Object> body) {
        MDC.put(KEY_GLOBAL_EXCEPTION_HANDLER_STATUS_CODE, String.valueOf(body.getStatusCode()));
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}