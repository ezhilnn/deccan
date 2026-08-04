package io.deccan.worker.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import io.deccan.worker.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionTaskResponse {

    private UUID id;

    private UUID executionId;
    private Integer workflowVersion;

    private String nodeId;

    private String nodeType;

    private JsonNode configuration;

    private TaskStatus status;

    private Integer retryCount;

    private Instant createdAt;

    private Instant updatedAt;

}