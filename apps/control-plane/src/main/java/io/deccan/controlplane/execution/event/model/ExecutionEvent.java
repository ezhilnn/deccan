package io.deccan.controlplane.execution.event.model;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class ExecutionEvent {

    private UUID executionId;

    private UUID workflowId;

    private String type;

    private OffsetDateTime timestamp;

}