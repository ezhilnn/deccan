package io.deccan.controlplane.secret.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSecretRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String value;

}