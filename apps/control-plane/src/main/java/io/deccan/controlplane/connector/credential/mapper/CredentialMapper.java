package io.deccan.controlplane.connector.credential.mapper;

import io.deccan.controlplane.connector.credential.dto.response.CredentialResponse;
import io.deccan.controlplane.connector.credential.entity.ConnectorCredential;
import org.springframework.stereotype.Component;

@Component
public class CredentialMapper {

    public CredentialResponse toResponse(
            ConnectorCredential credential){

        return CredentialResponse.builder()
                .id(credential.getId())
                .organizationId(
                        credential.getOrganization().getId())
                .name(credential.getName())
                .type(credential.getType())
                .provider(credential.getProvider())
                .secretReference(
                        credential.getSecretReference())
                .enabled(
                        credential.getEnabled())
                .createdAt(
                        credential.getCreatedAt())
                .updatedAt(
                        credential.getUpdatedAt())
                .build();

    }

}