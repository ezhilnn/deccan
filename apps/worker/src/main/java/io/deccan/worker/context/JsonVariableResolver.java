package io.deccan.worker.context;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonVariableResolver {

    JsonNode resolve(
            JsonNode node);

}