package io.deccan.worker.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionTaskResponse {

    private UUID id;

    private UUID executionId;

    private UUID workflowVersionId;

    private String nodeId;

    private String nodeType;

    private JsonNode configuration;

    private Integer retryCount;

    private Instant createdAt;

    private Instant updatedAt;

}