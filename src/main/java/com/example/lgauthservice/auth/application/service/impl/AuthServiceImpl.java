package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.AuthService;
import com.example.lgauthservice.auth.domain.entities.EmailVerificationToken;
import com.example.lgauthservice.auth.domain.entities.Role;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.enums.Provider;
import com.example.lgauthservice.auth.enums.Status;
import com.example.lgauthservice.auth.presentation.models.request.RegisterRequest;
import com.example.lgauthservice.auth.presentation.models.response.RegisterResponse;
import com.example.lgauthservice.auth.presentation.repository.EmailVerificationRepository;
import com.example.lgauthservice.auth.presentation.repository.RoleRepository;
import com.example.lgauthservice.auth.presentation.repository.UserRepository;
import com.example.lgauthservice.shared.domain.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
       // validate
        validateRegisterRequest(registerRequest);

        // create user
        User user = createUser(registerRequest);

        // assign role
        assignRoleToUser(user);

        // verify email
        String verifyToken = createVerificationToken(user);


        return RegisterResponse.builder().email(registerRequest.getEmail()).build();
    }

    public void validateRegisterRequest(RegisterRequest registerRequest) {
        if(!registerRequest.getPassword().equals(registerRequest.getPasswordConfirmation())) {
            throw new BadRequestException("Password not confirmed", null);
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already in use", null);
        }
    }

    public User createUser(RegisterRequest registerRequest) {
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getEmail()))
                .status(Status.PENDING)
                .provider(Provider.LOCAL)
                .build();
        return userRepository.save(user);
    }

    public void assignRoleToUser(User user) {
        Role role = roleRepository.findByCode("ADMIN").orElseThrow(() -> new BadRequestException("error", null));

        user.setRoleId(role.getId());
        userRepository.save(user);
    }

    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailVerificationToken = EmailVerificationToken.builder()
                .userId(user.getId())
                .token(token)
                .expiredAt(Instant.now().plusSeconds(3600)).build();
        emailVerificationRepository.save(emailVerificationToken);
        return token;
    }
}
