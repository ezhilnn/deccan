package io.deccan.controlplane.security.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.security.dto.LoginRequest;
import io.deccan.controlplane.security.dto.LoginResponse;
import io.deccan.controlplane.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(

                ApiResponse.<LoginResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Login successful")
                        .data(response)
                        .build()

        );

    }

}