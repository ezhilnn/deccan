package io.deccan.controlplane.secret.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.secret.dto.request.CreateSecretRequest;
import io.deccan.controlplane.secret.dto.request.UpdateSecretRequest;
import io.deccan.controlplane.secret.dto.response.SecretResponse;
import io.deccan.controlplane.secret.entity.Secret;
import io.deccan.controlplane.secret.mapper.SecretMapper;
import io.deccan.controlplane.secret.service.SecretService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/secrets")
@RequiredArgsConstructor
public class SecretController {

    private final SecretService service;

    private final SecretMapper mapper;

    @PreAuthorize("hasAuthority('secret.write')")
    @PostMapping
    public ApiResponse<SecretResponse> create(

            @Valid
            @RequestBody
            CreateSecretRequest request) {

        Secret secret =
                service.createSecret(request);

        return ApiResponse.<SecretResponse>builder()
                .status(201)
                .message("Secret created successfully")
                .data(mapper.toResponse(secret))
                .build();

    }

    @PreAuthorize("hasAuthority('secret.read')")
    @GetMapping("/{secretId}")
    public ApiResponse<SecretResponse> get(

            @PathVariable
            UUID secretId) {

        return ApiResponse.<SecretResponse>builder()
                .status(200)
                .message("Secret fetched successfully")
                .data(
                        mapper.toResponse(
                                service.getSecret(secretId)))
                .build();

    }

    @PreAuthorize("hasAuthority('secret.read')")
    @GetMapping("/organizations/{organizationId}")
    public ApiResponse<List<SecretResponse>> list(

            @PathVariable
            UUID organizationId) {

        List<SecretResponse> response =
                service.listSecrets(organizationId)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse.<List<SecretResponse>>builder()
                .status(200)
                .message("Secrets fetched successfully")
                .data(response)
                .build();

    }

    @PreAuthorize("hasAuthority('secret.write')")
    @PutMapping("/{secretId}")
    public ApiResponse<SecretResponse> update(

            @PathVariable
            UUID secretId,

            @Valid
            @RequestBody
            UpdateSecretRequest request) {

        Secret secret =
                service.updateSecret(
                        secretId,
                        request);

        return ApiResponse.<SecretResponse>builder()
                .status(200)
                .message("Secret updated successfully")
                .data(mapper.toResponse(secret))
                .build();

    }

    @PreAuthorize("hasAuthority('secret.write')")
    @DeleteMapping("/{secretId}")
    public ApiResponse<Void> delete(

            @PathVariable
            UUID secretId) {

        service.deleteSecret(secretId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Secret deleted successfully")
                .build();

    }

}