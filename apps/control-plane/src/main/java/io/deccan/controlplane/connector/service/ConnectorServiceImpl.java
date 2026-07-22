package io.deccan.controlplane.connector.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.connector.entity.Connector;
import io.deccan.controlplane.connector.enums.ConnectorType;
import io.deccan.controlplane.connector.repository.ConnectorRepository;
import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectorServiceImpl
        implements ConnectorService {

    private final ConnectorRepository repository;

    private final OrganizationRepository organizationRepository;

    @Override
    public Connector createConnector(

            UUID organizationId,

            String name,

            String displayName,

            ConnectorType type,

            String version,

            JsonNode configurationSchema) {

        if(repository.existsByNameAndVersion(
                name,
                version)){

            throw new IllegalArgumentException(
                    "Connector already exists");

        }

        Connector connector=new Connector();

        if(organizationId!=null){

            Organization organization=
                    organizationRepository.findById(
                            organizationId)
                            .orElseThrow();

            connector.setOrganization(
                    organization);

        }

        connector.setName(name);

        connector.setDisplayName(displayName);

        connector.setType(type);

        connector.setVersion(version);

        connector.setEnabled(true);

        connector.setConfigurationSchema(
                configurationSchema);

        return repository.save(connector);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Connector> getConnectors(){

        return repository.findAll();

    }

}