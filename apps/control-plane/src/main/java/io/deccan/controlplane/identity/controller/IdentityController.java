package io.deccan.controlplane.identity.controller;

import io.deccan.controlplane.identity.dto.request.CreateOrganizationRequest;
import io.deccan.controlplane.identity.dto.request.CreateRoleRequest;
import io.deccan.controlplane.identity.dto.request.CreateUserRequest;
import io.deccan.controlplane.identity.dto.response.OrganizationResponse;
import io.deccan.controlplane.identity.dto.response.RoleResponse;
import io.deccan.controlplane.identity.dto.response.UserResponse;
import io.deccan.controlplane.identity.mapper.IdentityMapper;
import io.deccan.controlplane.identity.service.IdentityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/identity")
@RequiredArgsConstructor
public class IdentityController {

    private final IdentityService identityService;
    private final IdentityMapper mapper;

    @PostMapping("/organizations")
    public OrganizationResponse createOrganization(
            @Valid @RequestBody CreateOrganizationRequest request) {

        return mapper.toResponse(
                identityService.createOrganization(
                        request.getName(),
                        request.getSlug()
                )
        );
    }

    @PostMapping("/users")
    public UserResponse createUser(
            @Valid @RequestBody CreateUserRequest request) {

        return mapper.toResponse(
                identityService.createUser(
                        request.getOrganizationId(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    @PostMapping("/roles")
    public RoleResponse createRole(
            @Valid @RequestBody CreateRoleRequest request) {

        return mapper.toResponse(
                identityService.createRole(
                        request.getOrganizationId(),
                        request.getName(),
                        request.getDescription()
                )
        );
    }

    @GetMapping("/organizations/{organizationId}/users")
    public List<UserResponse> users(
            @PathVariable UUID organizationId) {

        return identityService.getUsers(organizationId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}