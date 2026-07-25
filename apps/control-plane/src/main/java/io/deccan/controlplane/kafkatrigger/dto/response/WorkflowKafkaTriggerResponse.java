package io.deccan.controlplane.kafkatrigger.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkflowKafkaTriggerResponse {

    private UUID id;

    private UUID workflowId;

    private String topic;

    private Boolean enabled;

    private Instant createdAt;

    private Instant updatedAt;

}