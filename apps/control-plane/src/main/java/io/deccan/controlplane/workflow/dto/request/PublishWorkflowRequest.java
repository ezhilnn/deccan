package io.deccan.controlplane.workflow.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishWorkflowRequest {

    @NotNull
    private JsonNode definition;

}