package io.deccan.controlplane.identity.service.impl;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.entity.Role;
import io.deccan.controlplane.identity.entity.User;
import io.deccan.controlplane.identity.enums.OrganizationStatus;
import io.deccan.controlplane.identity.enums.UserStatus;
import io.deccan.controlplane.identity.exception.IdentityAlreadyExistsException;
import io.deccan.controlplane.identity.exception.IdentityNotFoundException;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.identity.repository.RoleRepository;
import io.deccan.controlplane.identity.repository.UserRepository;
import io.deccan.controlplane.identity.service.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class IdentityServiceImpl implements IdentityService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Organization createOrganization(String name, String slug) {

        if (organizationRepository.existsBySlug(slug)) {
            throw new IdentityAlreadyExistsException("Organization already exists");
        }

        Organization organization = new Organization();
        organization.setName(name);
        organization.setSlug(slug);
        organization.setStatus(OrganizationStatus.ACTIVE);

        return organizationRepository.save(organization);
    }

    @Override
    public User createUser(UUID organizationId,
                           String firstName,
                           String lastName,
                           String email,
                           String passwordHash) {

        if (userRepository.existsByEmail(email)) {
            throw new IdentityAlreadyExistsException("Email already exists");
        }

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IdentityNotFoundException("Organization not found"));

        User user = new User();
        user.setOrganization(organization);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public Role createRole(UUID organizationId,
                           String roleName,
                           String description) {

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IdentityNotFoundException("Organization not found"));

        roleRepository.findByOrganizationAndName(organization, roleName)
                .ifPresent(role -> {
                    throw new IdentityAlreadyExistsException("Role already exists");
                });

        Role role = new Role();
        role.setOrganization(organization);
        role.setName(roleName);
        role.setDescription(description);

        return roleRepository.save(role);
    }

    @Override
    public List<User> getUsers(UUID organizationId) {

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IdentityNotFoundException("Organization not found"));

        return organization.getUsers()
                .stream()
                .toList();
    }
}