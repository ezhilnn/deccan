package io.deccan.controlplane.execution.service;

import com.fasterxml.jackson.databind.JsonNode;

import io.deccan.controlplane.execution.context.dto.response.ExecutionContextResponse;
import io.deccan.controlplane.execution.entity.WorkflowExecution;

import java.util.List;
import java.util.UUID;

public interface ExecutionService {

    WorkflowExecution executeWorkflow(
            UUID workflowId,
            JsonNode input
    );

    WorkflowExecution getExecution(
            UUID executionId
    );

    List<WorkflowExecution> getExecutions(
            UUID workflowId
    );
    void cancelExecution(
        UUID executionId);
    WorkflowExecution retryExecution(
        UUID executionId);
    ExecutionContextResponse getExecutionContext(
        UUID executionId);
        

}