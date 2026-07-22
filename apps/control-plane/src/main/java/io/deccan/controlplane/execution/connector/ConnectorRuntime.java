package io.deccan.controlplane.execution.connector;

public interface ConnectorRuntime {

    boolean supports(
            String connector);

    ConnectorResponse execute(
            ConnectorRequest request);

}