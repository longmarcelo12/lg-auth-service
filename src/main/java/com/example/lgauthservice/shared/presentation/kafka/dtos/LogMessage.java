package com.example.lgauthservice.shared.presentation.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {
    private String id;
    private String updatedAt;
    private String level;
    private Long userId;
    private String templatePath;
    private String serviceName;
    private Metadata metadata;
    private String requestId;
    private String transactionId;
    private String createdAt;
    private Integer __v;
}
