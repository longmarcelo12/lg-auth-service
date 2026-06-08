package com.example.lgauthservice.auth.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name="email_verification_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationToken extends BaseEntity{
    private long userId;
    private String token;
    private Instant expiredAt;
}
