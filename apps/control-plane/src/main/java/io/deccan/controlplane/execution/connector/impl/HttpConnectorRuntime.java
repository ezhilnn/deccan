package io.deccan.controlplane.execution.connector.impl;

import io.deccan.controlplane.execution.connector.ConnectorRequest;
import io.deccan.controlplane.execution.connector.ConnectorResponse;
import io.deccan.controlplane.execution.connector.ConnectorRuntime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class HttpConnectorRuntime
        implements ConnectorRuntime {

    @Override
    public boolean supports(
            String connector){

        return "http".equalsIgnoreCase(connector);

    }

    @Override
    public ConnectorResponse execute(
            ConnectorRequest request){

        log.info(
                "Executing HTTP connector");

        return ConnectorResponse.builder()
                .success(true)
                .body(
                        Map.of(
                                "status",200,
                                "body","Mock HTTP Response"
                        ))
                .build();

    }

}