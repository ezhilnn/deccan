package io.deccan.controlplane.execution.mapper;

import io.deccan.controlplane.execution.dto.response.ExecutionResponse;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import org.springframework.stereotype.Component;

@Component
public class ExecutionMapper {

    public ExecutionResponse toResponse(
            WorkflowExecution execution){

        return ExecutionResponse.builder()
                .id(execution.getId())
                .workflowId(
                        execution.getWorkflow().getId())
                .workflowVersion(
                        execution.getWorkflowVersion())
                .status(execution.getStatus())
                .startedAt(execution.getStartedAt())
                .finishedAt(execution.getFinishedAt())
                .build();

    }

}