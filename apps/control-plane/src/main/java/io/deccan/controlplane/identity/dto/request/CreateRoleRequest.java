package io.deccan.controlplane.identity.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleRequest {

    private UUID organizationId;

    @NotBlank
    private String name;

    private String description;

}