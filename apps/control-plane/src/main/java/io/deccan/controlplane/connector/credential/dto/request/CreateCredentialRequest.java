package io.deccan.controlplane.connector.credential.dto.request;

import io.deccan.controlplane.connector.credential.enums.CredentialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCredentialRequest {

    @NotNull
    private UUID organizationId;

    @NotBlank
    private String name;

    @NotNull
    private CredentialType type;

    @NotBlank
    private String provider;

    @NotBlank
    private String secretReference;

}