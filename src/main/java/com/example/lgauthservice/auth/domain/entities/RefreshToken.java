package com.example.lgauthservice.auth.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="refresh_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseEntity {
    private long userId;
    private String token;
    private Instant expiresAt;
    private boolean revoked;
}
