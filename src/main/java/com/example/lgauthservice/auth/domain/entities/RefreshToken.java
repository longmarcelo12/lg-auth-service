package com.example.lgauthservice.auth.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="refresh_tokens")
public class RefreshToken extends BaseEntity {
    private long userId;
    private String token;
    private Instant expiresAt;
    private boolean revoked;
}
