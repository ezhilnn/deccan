package io.deccan.controlplane.execution.node.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class NodeExecutionResponse {

    private UUID id;

    private String nodeId;

    private String nodeType;

    private ExecutionStatus status;

    private OffsetDateTime startedAt;

    private OffsetDateTime finishedAt;

    private Long durationMs;

    private JsonNode output;

    private String errorMessage;

}