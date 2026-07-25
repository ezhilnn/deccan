package io.deccan.controlplane.scheduler.dto.response;

import io.deccan.controlplane.scheduler.enums.ScheduleType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkflowScheduleResponse {

    private UUID id;

    private UUID workflowId;

    private ScheduleType type;

    private String cronExpression;

    private Boolean enabled;

    private Instant createdAt;

    private Instant updatedAt;

}