package io.deccan.controlplane.connector.credential.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.connector.credential.dto.request.CreateCredentialRequest;
import io.deccan.controlplane.connector.credential.dto.response.CredentialResponse;
import io.deccan.controlplane.connector.credential.mapper.CredentialMapper;
import io.deccan.controlplane.connector.credential.service.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService service;

    private final CredentialMapper mapper;

    @PreAuthorize("hasAuthority('secret.write')")
    @PostMapping
    public ApiResponse<CredentialResponse> create(
            @Valid
            @RequestBody
            CreateCredentialRequest request){

        return ApiResponse
                .<CredentialResponse>builder()
                .status(201)
                .message("Credential created successfully")
                .data(
                        mapper.toResponse(
                                service.createCredential(
                                        request.getOrganizationId(),
                                        request.getName(),
                                        request.getType(),
                                        request.getProvider(),
                                        request.getSecretReference()
                                )
                        )
                )
                .build();
    }

    @PreAuthorize("hasAuthority('secret.read')")
    @GetMapping("/organization/{organizationId}")
    public ApiResponse<List<CredentialResponse>> list(
            @PathVariable UUID organizationId){

        return ApiResponse
                .<List<CredentialResponse>>builder()
                .status(200)
                .message("Credentials fetched successfully")
                .data(
                        service.getCredentials(
                                organizationId)
                                .stream()
                                .map(mapper::toResponse)
                                .toList()
                )
                .build();
    }

    @PreAuthorize("hasAuthority('secret.read')")
    @GetMapping("/{credentialId}")
    public ApiResponse<CredentialResponse> get(
            @PathVariable UUID credentialId){

        return ApiResponse
                .<CredentialResponse>builder()
                .status(200)
                .message("Credential fetched successfully")
                .data(
                        mapper.toResponse(
                                service.getCredential(
                                        credentialId)
                        )
                )
                .build();
    }

}