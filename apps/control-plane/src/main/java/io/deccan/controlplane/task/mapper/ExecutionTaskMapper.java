package io.deccan.controlplane.task.mapper;

import io.deccan.controlplane.task.dto.response.ExecutionTaskResponse;
import io.deccan.controlplane.task.entity.ExecutionTask;
import org.springframework.stereotype.Component;

@Component
public class ExecutionTaskMapper {

    public ExecutionTaskResponse toResponse(
            ExecutionTask task){

        return ExecutionTaskResponse.builder()

                .id(task.getId())

                .executionId(task.getExecutionId())

                .workerId(task.getWorkerId())

                .nodeId(
                        task.getNodeId())

                .nodeType(
                        task.getNodeType())

                .status(
                        task.getStatus())

                .leaseUntil(
                        task.getLeaseUntil())

                .startedAt(
                        task.getStartedAt())

                .completedAt(
                        task.getCompletedAt())

                .build();

    }

}