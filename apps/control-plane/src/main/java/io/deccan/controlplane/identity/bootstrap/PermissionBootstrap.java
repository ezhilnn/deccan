package io.deccan.controlplane.identity.bootstrap;

import io.deccan.controlplane.bootstrap.config.BootstrapProperties;
import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.entity.Permission;
import io.deccan.controlplane.identity.entity.Role;
import io.deccan.controlplane.identity.entity.User;
import io.deccan.controlplane.identity.enums.OrganizationStatus;
import io.deccan.controlplane.identity.enums.UserStatus;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.identity.repository.PermissionRepository;
import io.deccan.controlplane.identity.repository.RoleRepository;
import io.deccan.controlplane.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class PermissionBootstrap implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final BootstrapProperties bootstrapProperties;

    @Override
    public void run(String... args) {

        createPermission("workflow.read");
        createPermission("workflow.write");
        createPermission("workflow.execute");

        createPermission("connector.read");
        createPermission("connector.write");

        createPermission("secret.read");
        createPermission("secret.write");

        createPermission("user.read");
        createPermission("user.write");

        createPermission("role.read");
        createPermission("role.write");
        createPermission("artifact.read");
        createPermission("artifact.write");
        createPermission("workflow.update");
        if (bootstrapProperties.isEnabled()) {
            bootstrapAdmin();
        }

    }

    private void createPermission(String name) {

        if (permissionRepository.existsByName(name)) {
            return;
        }

        Permission permission = new Permission();
        permission.setName(name);

        permissionRepository.save(permission);

    }

    private void bootstrapAdmin() {

       if (userRepository.existsByEmail(
                bootstrapProperties.getAdmin().getEmail())) {

                Organization organization =
                        organizationRepository
                                .findBySlug(
                                        bootstrapProperties
                                                .getOrganization()
                                                .getSlug())
                                .orElse(null);

                if (organization != null) {

                        roleRepository
                                .findByOrganizationAndName(
                                        organization,
                                        "ADMIN")
                                .ifPresent(role -> {

                                role.setPermissions(
                                        new HashSet<>(
                                                permissionRepository.findAll()));

                                roleRepository.save(role);

                                });

                }

                return;

        }

        Organization organization = new Organization();

        organization.setName(
                bootstrapProperties.getOrganization().getName());

        organization.setSlug(
                bootstrapProperties.getOrganization().getSlug());

        organization.setStatus(OrganizationStatus.ACTIVE);

        organization =
                organizationRepository.save(organization);

        Role adminRole = new Role();

        adminRole.setOrganization(organization);
        adminRole.setName("ADMIN");
        adminRole.setDescription("Platform Administrator");

        adminRole.setPermissions(
                new HashSet<>(permissionRepository.findAll())
        );

        adminRole =
                roleRepository.save(adminRole);

        User admin = new User();

        admin.setOrganization(organization);

        admin.setFirstName(
                bootstrapProperties.getAdmin().getFirstName());

        admin.setLastName(
                bootstrapProperties.getAdmin().getLastName());

        admin.setEmail(
                bootstrapProperties.getAdmin().getEmail());

        admin.setPasswordHash(
                passwordEncoder.encode(
                        bootstrapProperties.getAdmin().getPassword()));

        admin.setStatus(UserStatus.ACTIVE);

        admin.getRoles().add(adminRole);

        userRepository.save(admin);

    }

}