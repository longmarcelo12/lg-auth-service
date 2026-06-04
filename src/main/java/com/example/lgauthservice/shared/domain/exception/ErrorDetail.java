package com.example.lgauthservice.shared.domain.exception;


import com.example.lgauthservice.shared.infrastructure.web.response.ApiResponse;
import com.example.lgauthservice.shared.utils.Utilities;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ErrorDetail {
    private String serviceName;
    private Object code;
    private String description;
    private Object data;

    public ErrorDetail(String serviceName, Object code, String description) {
        this.serviceName = serviceName;
        this.code = code;
        this.description = description;
    }

    public ErrorDetail(String serviceName, Object code, String description, Object data) {
        this.serviceName = serviceName;
        this.code = code;
        this.description = description;
        this.data = data;
    }

    public static ErrorDetail convertErrorDetail(String serviceName, String contentUtf8) {
        ApiResponse<Object> apiResponse = Utilities.copyProperties(contentUtf8, ApiResponse.class);
        if (ObjectUtils.allNull(apiResponse)) {
            return new ErrorDetail(
                    serviceName,
                    null,
                    "Invalid error response format",
                    contentUtf8
            );
        }
        return new ErrorDetail(
                serviceName,
                apiResponse.getStatusCode(),
                apiResponse.getMessage(),
                apiResponse.getData());
    }
}
