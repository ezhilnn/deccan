package io.deccan.controlplane.secret.service;

import io.deccan.controlplane.secret.dto.request.CreateSecretRequest;
import io.deccan.controlplane.secret.dto.request.UpdateSecretRequest;
import io.deccan.controlplane.secret.entity.Secret;

import java.util.List;
import java.util.UUID;

public interface SecretService {

    Secret createSecret(
            CreateSecretRequest request);

    Secret updateSecret(
            UUID secretId,
            UpdateSecretRequest request);

    Secret getSecret(
            UUID secretId);

    List<Secret> listSecrets(
            UUID organizationId);

    void deleteSecret(
            UUID secretId);

}