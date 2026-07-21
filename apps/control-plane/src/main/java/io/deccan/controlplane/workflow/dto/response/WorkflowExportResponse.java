package io.deccan.controlplane.workflow.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class WorkflowExportResponse {

    private UUID workflowId;

    private String workflowName;

    private Integer version;

    private JsonNode definition;

}