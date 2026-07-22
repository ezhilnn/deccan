package io.deccan.controlplane.connector.mapper;

import io.deccan.controlplane.connector.dto.response.ConnectorResponse;
import io.deccan.controlplane.connector.entity.Connector;
import org.springframework.stereotype.Component;

@Component
public class ConnectorMapper {

    public ConnectorResponse toResponse(
            Connector connector) {

        return ConnectorResponse.builder()
                .id(connector.getId())
                .credentialId(

                        connector.getCredential()==null

                                ? null

                                : connector.getCredential().getId()

                )
                .organizationId(
                        connector.getOrganization() == null
                                ? null
                                : connector.getOrganization().getId())
                .name(connector.getName())
                .displayName(connector.getDisplayName())
                .type(connector.getType())
                .version(connector.getVersion())
                .enabled(connector.getEnabled())
                .configurationSchema(connector.getConfigurationSchema())
                .createdAt(connector.getCreatedAt())
                .updatedAt(connector.getUpdatedAt())
                .build();

    }

}