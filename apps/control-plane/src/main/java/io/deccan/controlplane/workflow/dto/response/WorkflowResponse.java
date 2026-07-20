package io.deccan.controlplane.workflow.dto.response;

import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkflowResponse {

    private UUID id;

    private UUID organizationId;

    private String name;

    private String description;

    private WorkflowStatus status;

    private Integer currentVersion;

    private Instant createdAt;

    private Instant updatedAt;

}