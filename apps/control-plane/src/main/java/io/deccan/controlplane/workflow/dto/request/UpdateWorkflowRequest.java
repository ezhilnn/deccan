package io.deccan.controlplane.workflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWorkflowRequest {

    @NotBlank
    private String name;

    private String description;

}