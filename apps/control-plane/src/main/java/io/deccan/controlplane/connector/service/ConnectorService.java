package io.deccan.controlplane.connector.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.connector.entity.Connector;
import io.deccan.controlplane.connector.enums.ConnectorType;

import java.util.List;
import java.util.UUID;

public interface ConnectorService {

    Connector createConnector(

            UUID organizationId,

            String name,

            String displayName,

            ConnectorType type,

            String version,

            JsonNode configurationSchema

    );

    List<Connector> getConnectors();

}