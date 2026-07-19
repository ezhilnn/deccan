package io.deccan.controlplane.identity.service;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.entity.Role;
import io.deccan.controlplane.identity.entity.User;

import java.util.List;
import java.util.UUID;

public interface IdentityService {

    Organization createOrganization(String name, String slug);

    User createUser(
            UUID organizationId,
            String firstName,
            String lastName,
            String email,
            String passwordHash
    );

    Role createRole(
            UUID organizationId,
            String roleName,
            String description
    );

    List<User> getUsers(UUID organizationId);

}