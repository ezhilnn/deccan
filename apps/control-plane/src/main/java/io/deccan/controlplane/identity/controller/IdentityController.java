package io.deccan.controlplane.identity.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.identity.dto.request.CreateOrganizationRequest;
import io.deccan.controlplane.identity.dto.request.CreateRoleRequest;
import io.deccan.controlplane.identity.dto.request.CreateUserRequest;
import io.deccan.controlplane.identity.dto.response.OrganizationResponse;
import io.deccan.controlplane.identity.dto.response.RoleResponse;
import io.deccan.controlplane.identity.dto.response.UserResponse;
import io.deccan.controlplane.identity.entity.User;
import io.deccan.controlplane.identity.mapper.IdentityMapper;
import io.deccan.controlplane.identity.service.IdentityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/identity")
@RequiredArgsConstructor
public class IdentityController {

    private final IdentityService identityService;
    private final IdentityMapper mapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/organizations")
    public ApiResponse<OrganizationResponse> createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request) {

        OrganizationResponse response = mapper.toResponse(
                identityService.createOrganization(
                        request.getName(),
                        request.getSlug()
                )
        );

        return ApiResponse.<OrganizationResponse>builder()
                .status(201)
                .message("Organization created successfully")
                .data(response)
                .build();
    }

    @PreAuthorize("hasAuthority('user.write')")
    @PostMapping("/users")
    public ApiResponse<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse response =  mapper.toResponse(
                identityService.createUser(
                        request.getOrganizationId(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword()
                )

        );
        return ApiResponse.<UserResponse>builder()
                .status(201)
                .message("User created successfully")
                .data(response)
                .build();
        
    }

    @PreAuthorize("hasAuthority('role.write')")
    @PostMapping("/roles")
    public ApiResponse<RoleResponse> createRole(
            @Valid @RequestBody CreateRoleRequest request) {

        RoleResponse response = mapper.toResponse(
                identityService.createRole(
                        request.getOrganizationId(),
                        request.getName(),
                        request.getDescription()
                )
        );

        return ApiResponse.<RoleResponse>builder()
                .status(201)
                .message("Role created successfully")
                .data(response)
                .build();
    }

    @PreAuthorize("hasAuthority('user.read')")
    @GetMapping("/organizations/{organizationId}/users")
    public ApiResponse<List<UserResponse>> users(
            @PathVariable UUID organizationId) {

        List<UserResponse> response = identityService.getUsers(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ApiResponse.<List<UserResponse>>builder()
                .status(200)
                .message("Users fetched successfully")
                .data(response)
                .build();
    }
}