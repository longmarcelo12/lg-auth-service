package com.example.lgauthservice.shared.presentation.kafka.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class Metadata {
    private JsonNode headers;
    private JsonNode body;
    private JsonNode response;

    private String url;
    private String path;
    private String method;
    private Long durationMs;
}
