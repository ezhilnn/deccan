package io.deccan.controlplane.execution.connector;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectorRequest {

    private String connector;

    private JsonNode configuration;

}