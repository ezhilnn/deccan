package io.deccan.worker.service;

import io.deccan.worker.config.ControlPlaneProperties;
import io.deccan.worker.dto.request.LoginRequest;
import io.deccan.worker.dto.response.ApiResponse;
import io.deccan.worker.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RestClient restClient;
    private final ControlPlaneProperties properties;

    private volatile String token;

    public String getToken() {

        if (token != null) {
            return token;
        }

        log.info("Control Plane Base URL : {}", properties.getBaseUrl());
        log.info("Authenticating with Control Plane...");

        LoginRequest request = new LoginRequest();
        request.setEmail(properties.getEmail());
        request.setPassword(properties.getPassword());

        ApiResponse<LoginResponse> response =
                restClient.post()
                        .uri("/auth/login")
                        .body(request)
                        .retrieve()
                        .body(new ParameterizedTypeReference<ApiResponse<LoginResponse>>() {});

        if (response == null || response.getData() == null) {
            throw new IllegalStateException("Authentication failed.");
        }

        token = response.getData().getAccessToken();

        log.info("Successfully authenticated.");

        return token;
    }
}