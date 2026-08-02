package io.deccan.controlplane.secret.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.secret.entity.Secret;

public interface SecretRepository
        extends JpaRepository<Secret, UUID> {

    Optional<Secret> findByOrganizationAndName(
            Organization organization,
            String name);

    boolean existsByOrganizationAndName(
            Organization organization,
            String name);

    List<Secret> findByOrganization(
            Organization organization);

}