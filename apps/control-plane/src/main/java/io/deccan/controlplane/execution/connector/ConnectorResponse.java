package io.deccan.controlplane.execution.connector;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectorResponse {

    private boolean success;

    private Object body;

    private String error;

}