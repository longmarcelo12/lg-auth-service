package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.EmailService;
import com.example.lgauthservice.auth.domain.entities.EmailVerificationToken;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.enums.Status;
import com.example.lgauthservice.auth.presentation.models.response.VerifyTokenResponse;
import com.example.lgauthservice.auth.presentation.repository.EmailVerificationRepository;
import com.example.lgauthservice.auth.presentation.repository.UserRepository;
import com.example.lgauthservice.shared.domain.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor()
@Transactional
public class EmailServiceImpl implements EmailService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    public void sendVerificationEmail(String email, String token) {

    }

    public VerifyTokenResponse verifyToken(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationRepository
                .findByToken(token)
                .orElseThrow(() -> new BadRequestException("Token not found", null));

        if(emailVerificationToken.getExpiredAt().isBefore(Instant.now())) {
            throw new BadRequestException("Token is expired", null, null);
        }

        User user = userRepository.getById(emailVerificationToken.getUserId());
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        emailVerificationRepository.delete(emailVerificationToken);

        return VerifyTokenResponse.builder().email(user.getEmail()).message("Verify Successfully!").build();
    }
}
