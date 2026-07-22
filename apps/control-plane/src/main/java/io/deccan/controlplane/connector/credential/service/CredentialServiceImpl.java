package io.deccan.controlplane.connector.credential.service;

import io.deccan.controlplane.connector.credential.entity.ConnectorCredential;
import io.deccan.controlplane.connector.credential.enums.CredentialType;
import io.deccan.controlplane.connector.credential.repository.ConnectorCredentialRepository;
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
public class CredentialServiceImpl
        implements CredentialService {

    private final ConnectorCredentialRepository repository;

    private final OrganizationRepository organizationRepository;

    @Override
    public ConnectorCredential createCredential(

            UUID organizationId,

            String name,

            CredentialType type,

            String provider,

            String secretReference){

        Organization organization =
                organizationRepository.findById(
                        organizationId)
                        .orElseThrow();

        if(repository.existsByOrganizationAndName(
                organization,
                name)){

            throw new IllegalArgumentException(
                    "Credential already exists");

        }

        ConnectorCredential credential =
                new ConnectorCredential();

        credential.setOrganization(
                organization);

        credential.setName(name);

        credential.setType(type);

        credential.setProvider(provider);

        credential.setSecretReference(
                secretReference);

        credential.setEnabled(true);

        return repository.save(
                credential);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ConnectorCredential> getCredentials(
            UUID organizationId){

        Organization organization =
                organizationRepository.findById(
                        organizationId)
                        .orElseThrow();

        return repository.findByOrganization(
                organization);

    }

    @Override
    @Transactional(readOnly = true)
    public ConnectorCredential getCredential(
            UUID credentialId){

        return repository.findById(
                credentialId)
                .orElseThrow();

    }

}