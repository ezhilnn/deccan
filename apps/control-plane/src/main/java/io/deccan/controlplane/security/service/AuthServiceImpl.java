package io.deccan.controlplane.security.service;

import io.deccan.controlplane.identity.entity.User;
import io.deccan.controlplane.identity.enums.UserStatus;
import io.deccan.controlplane.identity.exception.IdentityNotFoundException;
import io.deccan.controlplane.identity.repository.UserRepository;
import io.deccan.controlplane.security.config.JwtProperties;
import io.deccan.controlplane.security.dto.LoginRequest;
import io.deccan.controlplane.security.dto.LoginResponse;
import io.deccan.controlplane.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final JwtProperties jwtProperties;

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailAndStatus(
                        request.getEmail(),
                        UserStatus.ACTIVE
                )
                .orElseThrow(() ->
                        new IdentityNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new BadCredentialsException("Invalid credentials");

        }

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpiration())
                .build();

    }

}