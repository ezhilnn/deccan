package io.deccan.controlplane.connector.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.connector.enums.ConnectorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateConnectorRequest {

    private UUID organizationId;

    @NotBlank
    private String name;

    @NotBlank
    private String displayName;

    @NotNull
    private ConnectorType type;

    @NotBlank
    private String version;

    @NotNull
    private JsonNode configurationSchema;

}