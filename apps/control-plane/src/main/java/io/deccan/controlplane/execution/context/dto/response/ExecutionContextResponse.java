package io.deccan.controlplane.execution.context.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ExecutionContextResponse {

    private Map<String, JsonNode> nodeOutputs;

}