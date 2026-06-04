package com.example.lgauthservice.shared.presentation.feign.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ConfigDetailResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("_id")
    private String id;
    private String service;
    private String key;
    private T value;
    private String updatedAt;
    private String createdAt;
//    @JsonProperty("__v")
//    private Integer __v;
}
