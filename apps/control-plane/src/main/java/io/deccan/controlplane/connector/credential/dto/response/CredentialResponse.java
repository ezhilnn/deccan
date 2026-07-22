package io.deccan.controlplane.connector.credential.dto.response;

import io.deccan.controlplane.connector.credential.enums.CredentialType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class CredentialResponse {

    private UUID id;

    private UUID organizationId;

    private String name;

    private CredentialType type;

    private String provider;

    private String secretReference;

    private Boolean enabled;

    private Instant createdAt;

    private Instant updatedAt;

}