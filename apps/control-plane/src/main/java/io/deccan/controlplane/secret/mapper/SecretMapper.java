package io.deccan.controlplane.secret.mapper;

import io.deccan.controlplane.secret.dto.response.SecretResponse;
import io.deccan.controlplane.secret.entity.Secret;
import org.springframework.stereotype.Component;

@Component
public class SecretMapper {

    public SecretResponse toResponse(
            Secret secret) {

        return SecretResponse.builder()
                .id(secret.getId())
                .organizationId(secret.getOrganization().getId())
                .name(secret.getName())
                .description(secret.getDescription())
                .createdAt(secret.getCreatedAt())
                .updatedAt(secret.getUpdatedAt())
                .build();

    }

}