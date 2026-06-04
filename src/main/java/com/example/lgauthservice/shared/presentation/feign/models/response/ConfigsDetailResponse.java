package com.example.lgauthservice.shared.presentation.feign.models.response;

import lombok.Data;


@Data
public class ConfigsDetailResponse {

    private String service;
    private String key;
    private String value;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    private LocalDateTime createdAt;
}
