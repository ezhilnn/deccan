package io.deccan.controlplane.connector.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.connector.enums.ConnectorType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ConnectorResponse {

    private UUID id;
    private UUID credentialId;

    private UUID organizationId;

    private String name;

    private String displayName;

    private ConnectorType type;

    private String version;

    private Boolean enabled;

    private JsonNode configurationSchema;

    private Instant createdAt;

    private Instant updatedAt;

}