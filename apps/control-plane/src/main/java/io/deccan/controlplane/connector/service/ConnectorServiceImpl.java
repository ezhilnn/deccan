package io.deccan.controlplane.connector.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.connector.entity.Connector;
import io.deccan.controlplane.connector.enums.ConnectorType;
import io.deccan.controlplane.connector.repository.ConnectorRepository;
import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.deccan.controlplane.connector.validation.ConnectorValidator;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectorServiceImpl
        implements ConnectorService {

    private final ConnectorRepository connectorRepository;

    private final OrganizationRepository organizationRepository;
    private final ConnectorValidator connectorValidator;

    @Override
    public Connector createConnector(
            UUID organizationId,
            String name,
            String displayName,
            ConnectorType type,
            String version,
            JsonNode configurationSchema) {

        if (connectorRepository.existsByNameAndVersion(
                name,
                version)) {

            throw new IllegalArgumentException(
                    "Connector already exists");

        }

        Connector connector = new Connector();

        if (organizationId != null) {

            Organization organization =
                    organizationRepository.findById(
                                    organizationId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Organization not found"));

            connector.setOrganization(organization);

        }

        connector.setName(name);
        connector.setDisplayName(displayName);
        connector.setType(type);
        connector.setVersion(version);
        connector.setEnabled(true);
        connector.setConfigurationSchema(configurationSchema);
        connectorValidator.validate(connector);

        return connectorRepository.save(connector);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Connector> getConnectors() {

        return connectorRepository.findAll();

    }

    @Override
    @Transactional(readOnly = true)
    public Connector getConnector(
            UUID connectorId) {

        return connectorRepository.findById(connectorId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Connector not found"));

    }
    @Override
    public Connector updateConnector(
            UUID connectorId,
            String displayName,
            ConnectorType type,
            JsonNode configurationSchema,
            Boolean enabled) {

        Connector connector =
                connectorRepository.findById(connectorId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Connector not found"));

        connector.setDisplayName(displayName);
        connector.setType(type);
        connector.setConfigurationSchema(configurationSchema);
        connector.setEnabled(enabled);

        return connectorRepository.save(connector);

    }

    @Override
    public void deleteConnector(
            UUID connectorId) {

        Connector connector =
                connectorRepository.findById(connectorId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Connector not found"));

        connectorRepository.delete(connector);

    }

}