package io.deccan.controlplane.connector.exception;

public class ConnectorNotFoundException
        extends RuntimeException {

    public ConnectorNotFoundException(String message) {
        super(message);
    }

}