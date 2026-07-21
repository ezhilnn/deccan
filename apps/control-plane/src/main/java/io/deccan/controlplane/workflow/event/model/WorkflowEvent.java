package io.deccan.controlplane.workflow.event.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkflowEvent {

    private UUID workflowId;

    private UUID organizationId;

    private Integer version;

    private WorkflowEventType type;

    private Instant timestamp;

}