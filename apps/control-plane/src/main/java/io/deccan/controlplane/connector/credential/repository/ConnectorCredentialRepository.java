package io.deccan.controlplane.connector.credential.repository;

import io.deccan.controlplane.connector.credential.entity.ConnectorCredential;
import io.deccan.controlplane.identity.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectorCredentialRepository
        extends JpaRepository<ConnectorCredential,UUID> {

    Optional<ConnectorCredential>
    findByOrganizationAndName(

            Organization organization,

            String name

    );

    boolean existsByOrganizationAndName(

            Organization organization,

            String name

    );

    List<ConnectorCredential>
    findByOrganization(

            Organization organization

    );

}