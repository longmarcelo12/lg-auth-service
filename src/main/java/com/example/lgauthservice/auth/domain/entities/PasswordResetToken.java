package com.example.lgauthservice.auth.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="password_reset_tokens")
public class PasswordResetToken extends BaseEntity{
    private String token;
    private Instant expiresAt;
    private boolean used;
    private long userId;
}
