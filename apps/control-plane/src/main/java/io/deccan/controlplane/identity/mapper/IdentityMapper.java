package io.deccan.controlplane.identity.mapper;

import io.deccan.controlplane.identity.dto.response.OrganizationResponse;
import io.deccan.controlplane.identity.dto.response.RoleResponse;
import io.deccan.controlplane.identity.dto.response.UserResponse;
import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.entity.Role;
import io.deccan.controlplane.identity.entity.User;
import org.springframework.stereotype.Component;

@Component
public class IdentityMapper {

    public OrganizationResponse toResponse(Organization organization) {

        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .slug(organization.getSlug())
                .build();
    }

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public RoleResponse toResponse(Role role) {

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

}