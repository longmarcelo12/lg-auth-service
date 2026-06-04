package com.example.lgauthservice.shared.presentation.feign.models.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
public class ConfigsServiceResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer statusCode;
    private String message;
    private String requestId;
    private Map<String, ConfigsDetailResponse> data;
}
