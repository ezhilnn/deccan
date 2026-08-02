package io.deccan.controlplane.secret.service.impl;

import io.deccan.controlplane.secret.service.SecretEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SecretEncryptionServiceImpl
        implements SecretEncryptionService {

    private static final String ALGORITHM = "AES";

    @Value("${deccan.secret.encryption-key}")
    private String encryptionKey;

    @Override
    public String encrypt(
            String value) {

        try {

            SecretKeySpec key =
                    new SecretKeySpec(
                            encryptionKey.getBytes(StandardCharsets.UTF_8),
                            ALGORITHM);

            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    key);

            byte[] encrypted =
                    cipher.doFinal(
                            value.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder()
                    .encodeToString(encrypted);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to encrypt secret",
                    ex);

        }

    }

    @Override
    public String decrypt(
            String encryptedValue) {

        try {

            SecretKeySpec key =
                    new SecretKeySpec(
                            encryptionKey.getBytes(StandardCharsets.UTF_8),
                            ALGORITHM);

            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    key);

            byte[] decrypted =
                    cipher.doFinal(
                            Base64.getDecoder()
                                    .decode(encryptedValue));

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to decrypt secret",
                    ex);

        }

    }

}