package com.example.lgauthservice.auth.presentation.repository;

import com.example.lgauthservice.auth.domain.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {
}
