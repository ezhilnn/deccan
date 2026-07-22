package io.deccan.controlplane.execution.dto.response;

import io.deccan.controlplane.execution.enums.ExecutionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class ExecutionResponse {

    private UUID id;

    private UUID workflowId;

    private Integer workflowVersion;

    private ExecutionStatus status;

    private OffsetDateTime startedAt;

    private OffsetDateTime finishedAt;

}