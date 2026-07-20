package io.deccan.controlplane.workflow.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
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

    private JsonNode definition;

    private Instant createdAt;

}