package io.deccan.controlplane.secret.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.time.Instant;

@Getter
@Builder
public class SecretResponse {

    private UUID id;

    private UUID organizationId;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

}