package io.deccan.controlplane.connector.repository;

import io.deccan.controlplane.connector.entity.Connector;
import io.deccan.controlplane.connector.enums.ConnectorType;
import io.deccan.controlplane.identity.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectorRepository
        extends JpaRepository<Connector, UUID> {

    boolean existsByNameAndVersion(
            String name,
            String version
    );

    Optional<Connector> findByNameAndVersion(
            String name,
            String version
    );

    List<Connector> findByOrganization(
            Organization organization
    );

    List<Connector> findByType(
            ConnectorType type
    );

    List<Connector> findByEnabledTrue();
    List<Connector> findByNameOrderByVersionDesc(
                String name
        );

}