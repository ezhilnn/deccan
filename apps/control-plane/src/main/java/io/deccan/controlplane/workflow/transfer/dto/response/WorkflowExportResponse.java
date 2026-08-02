package io.deccan.controlplane.workflow.transfer.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkflowExportResponse {

    private String name;

    private String description;

    private WorkflowStatus status;

    private Integer version;

    private JsonNode definition;

}