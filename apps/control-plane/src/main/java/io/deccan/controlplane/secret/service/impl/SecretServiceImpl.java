package io.deccan.controlplane.secret.service.impl;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.secret.dto.request.CreateSecretRequest;
import io.deccan.controlplane.secret.dto.request.UpdateSecretRequest;
import io.deccan.controlplane.secret.entity.Secret;
import io.deccan.controlplane.secret.repository.SecretRepository;
import io.deccan.controlplane.secret.service.SecretEncryptionService;
import io.deccan.controlplane.secret.service.SecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SecretServiceImpl
        implements SecretService {

    private final SecretRepository repository;

    private final OrganizationRepository organizationRepository;

    private final SecretEncryptionService encryptionService;

    @Override
    public Secret createSecret(
            CreateSecretRequest request) {

        Organization organization =
                organizationRepository.findById(
                        request.getOrganizationId())
                        .orElseThrow();

        if (repository.existsByOrganizationAndName(
                organization,
                request.getName())) {

            throw new IllegalArgumentException(
                    "Secret already exists");

        }

        Secret secret = new Secret();

        secret.setOrganization(organization);
        secret.setName(request.getName());
        secret.setDescription(request.getDescription());
        secret.setEncryptedValue(
                encryptionService.encrypt(
                        request.getValue()));

        return repository.save(secret);

    }

    @Override
    public Secret updateSecret(
            UUID secretId,
            UpdateSecretRequest request) {

        Secret secret =
                repository.findById(secretId)
                        .orElseThrow();

        secret.setName(request.getName());
        secret.setDescription(request.getDescription());
        secret.setEncryptedValue(
                encryptionService.encrypt(
                        request.getValue()));

        return repository.save(secret);

    }

    @Override
    @Transactional(readOnly = true)
    public Secret getSecret(
            UUID secretId) {

        return repository.findById(secretId)
                .orElseThrow();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Secret> listSecrets(
            UUID organizationId) {

        Organization organization =
                organizationRepository.findById(
                        organizationId)
                        .orElseThrow();

        return repository.findByOrganization(
                organization);

    }

    @Override
    public void deleteSecret(
            UUID secretId) {

        repository.deleteById(secretId);

    }

}