package io.deccan.controlplane.execution.context.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.context.entity.ExecutionContextEntity;
import io.deccan.controlplane.execution.entity.WorkflowExecution;

import java.util.List;

public interface ExecutionContextService {

    void saveNodeOutput(
            WorkflowExecution execution,
            String nodeId,
            JsonNode output);

    List<ExecutionContextEntity> getContext(
            WorkflowExecution execution);

}