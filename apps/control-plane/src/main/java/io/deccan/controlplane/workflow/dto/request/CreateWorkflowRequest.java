package io.deccan.controlplane.workflow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateWorkflowRequest {

    @NotNull
    private UUID organizationId;

    @NotBlank
    private String name;

    private String description;

}