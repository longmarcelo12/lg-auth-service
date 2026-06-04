package com.example.lgauthservice.shared.infrastructure.web.response;

import com.example.lgauthservice.shared.constants.Constants;
import com.example.lgauthservice.shared.utils.StringUtils;
import com.example.lgauthservice.shared.utils.converter.DateTimeJsonDeserializer;
import com.example.lgauthservice.shared.utils.converter.DateTimeJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.ThreadContext;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = -8903025058138925817L;
    private int statusCode; //Các lỗi kphai auth thì sẽ là 400
    private String message;
    private T data; //
    private String code;
    private String requestId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private Instant timestamp;

    private ApiResponse() {
    }

    public static class ApiResponseBuilder<T> {
        public ApiResponseBuilder() {
            this.requestId = ThreadContext.get(Constants.Headers.X_REQUEST_ID);
            if (null == this.requestId) {
                this.requestId = StringUtils.getUUID();
            }
            this.timestamp = Instant.now();
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }
    }
}
