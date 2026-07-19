package io.deccan.controlplane.identity.repository;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByOrganizationAndName(
            Organization organization,
            String name
    );

}