package com.example.lgauthservice.auth.application.service.impl;

import com.example.lgauthservice.auth.application.service.AuthService;
import com.example.lgauthservice.auth.application.service.EmailService;
import com.example.lgauthservice.auth.application.service.JwtService;
import com.example.lgauthservice.auth.application.service.RefreshTokenService;
import com.example.lgauthservice.auth.domain.entities.EmailVerificationToken;
import com.example.lgauthservice.auth.domain.entities.PasswordResetToken;
import com.example.lgauthservice.auth.domain.entities.Role;
import com.example.lgauthservice.auth.domain.entities.User;
import com.example.lgauthservice.auth.enums.Provider;
import com.example.lgauthservice.auth.enums.Status;
import com.example.lgauthservice.auth.infrastructure.config.JwtProperties;
import com.example.lgauthservice.auth.presentation.models.request.ForgotPasswordRequest;
import com.example.lgauthservice.auth.presentation.models.request.LoginRequest;
import com.example.lgauthservice.auth.presentation.models.request.RegisterRequest;
import com.example.lgauthservice.auth.presentation.models.response.LoginResponse;
import com.example.lgauthservice.auth.presentation.models.response.RegisterResponse;
import com.example.lgauthservice.auth.presentation.models.response.UserLoginData;
import com.example.lgauthservice.auth.presentation.repository.EmailVerificationRepository;
import com.example.lgauthservice.auth.presentation.repository.PasswordResetRepository;
import com.example.lgauthservice.auth.presentation.repository.RoleRepository;
import com.example.lgauthservice.auth.presentation.repository.UserRepository;
import com.example.lgauthservice.shared.domain.exception.BadRequestException;
import com.example.lgauthservice.shared.domain.exception.UnauthorizedException;
import com.example.lgauthservice.shared.utils.Utilities;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
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
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailService emailService;

    // register user
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

    // login
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email or password invalid 1"));

        validatePassword(loginRequest.getPassword(), user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        UserLoginData userLoginData = Utilities.copyProperties(user, UserLoginData.class);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtProperties.getAccessTokenExpiration())
                .tokenType("Bearer")
                .user(userLoginData)
                .build();
        return loginResponse;
    }

    public void validatePassword(String rawPassword, User user) {
        if(!user.getStatus().equals(Status.ACTIVE)) {
            throw new UnauthorizedException("Account is not active");
        }

        if(!user.getProvider().equals(Provider.LOCAL)) {
            throw new UnauthorizedException("Provider is not LOCAL");
        }

        if(!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UnauthorizedException("Email or password invalid 2");
        }
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
                .password(passwordEncoder.encode(registerRequest.getPassword()))
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

    // forgot password
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Optional<User> userOptional = userRepository.findByEmail(forgotPasswordRequest.getEmail());

        if(userOptional.isEmpty()) {
            throw new BadRequestException("error", null);
        }

        User user = userOptional.get();

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .expiresAt(Instant.now().plusSeconds(300))
                .token(token)
                .userId(user.getId())
                .used(false)
                .build();
        passwordResetRepository.save(passwordResetToken);

//        emailService.sendForgotPasswordEmail(forgotPasswordRequest.getEmail(), token);
    }
}
