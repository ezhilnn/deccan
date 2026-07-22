package io.deccan.controlplane.connector.credential.service;

import io.deccan.controlplane.connector.credential.entity.ConnectorCredential;
import io.deccan.controlplane.connector.credential.enums.CredentialType;

import java.util.List;
import java.util.UUID;

public interface CredentialService {

    ConnectorCredential createCredential(

            UUID organizationId,

            String name,

            CredentialType type,

            String provider,

            String secretReference

    );

    List<ConnectorCredential> getCredentials(
            UUID organizationId);

    ConnectorCredential getCredential(
            UUID credentialId);

}