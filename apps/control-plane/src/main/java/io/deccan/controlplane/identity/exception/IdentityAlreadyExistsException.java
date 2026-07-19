package io.deccan.controlplane.identity.exception;

public class IdentityAlreadyExistsException extends RuntimeException {

    public IdentityAlreadyExistsException(String message) {
        super(message);
    }

}