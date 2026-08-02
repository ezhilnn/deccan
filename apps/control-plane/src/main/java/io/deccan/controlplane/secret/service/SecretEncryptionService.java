package io.deccan.controlplane.secret.service;

public interface SecretEncryptionService {

    String encrypt(
            String value);

    String decrypt(
            String encryptedValue);

}