package io.deccan.controlplane.execution.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionRequest {

    private JsonNode input;

}