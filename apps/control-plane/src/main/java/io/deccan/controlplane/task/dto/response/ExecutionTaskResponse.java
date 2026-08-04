package io.deccan.controlplane.task.dto.response;

import io.deccan.controlplane.task.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

@Getter
@Builder
public class ExecutionTaskResponse {

    private UUID id;

    private UUID executionId;

    private UUID workerId;

    private String nodeId;

    private String nodeType;

    private TaskStatus status;

    private Instant leaseUntil;

    private Instant startedAt;

    private Instant completedAt;
    private JsonNode configuration;
    private Integer retryCount;
    private Integer workflowVersion;

}