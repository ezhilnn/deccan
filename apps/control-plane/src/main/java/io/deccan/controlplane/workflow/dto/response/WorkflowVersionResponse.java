package io.deccan.controlplane.workflow.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkflowVersionResponse {

    private UUID id;

    private Integer version;

    private Boolean published;

    private String definition;

    private Instant createdAt;

}