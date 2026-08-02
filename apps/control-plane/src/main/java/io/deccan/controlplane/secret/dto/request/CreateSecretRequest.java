package io.deccan.controlplane.secret.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSecretRequest {

    @NotNull
    private UUID organizationId;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String value;

}