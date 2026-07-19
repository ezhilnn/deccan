package io.deccan.controlplane.identity.exception;

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String message) {
        super(message);
    }

}