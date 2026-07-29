package io.deccan.worker.connector;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectorResult {

    private boolean success;

    private JsonNode output;

    private String errorMessage;

}