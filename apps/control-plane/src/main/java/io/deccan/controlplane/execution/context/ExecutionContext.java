package io.deccan.controlplane.execution.context;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import lombok.Builder;
import lombok.Getter;
import io.deccan.controlplane.execution.context.model.NodeResult;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ExecutionContext {

    private WorkflowExecution execution;

    private JsonNode input;

    @Builder.Default
    private Map<String,Object> variables =
            new HashMap<>();

    @Builder.Default
private Map<String,NodeResult> nodeOutputs =
        new HashMap<>();

}