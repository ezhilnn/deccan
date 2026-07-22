package io.deccan.controlplane.execution.connector.resolver;

import io.deccan.controlplane.execution.connector.ConnectorRuntime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConnectorRuntimeResolver {

    private final List<ConnectorRuntime> runtimes;

    public ConnectorRuntime resolve(
            String connector) {

        return runtimes.stream()
                .filter(runtime ->
                        runtime.supports(connector))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No connector runtime registered for: "
                                        + connector));

    }

}