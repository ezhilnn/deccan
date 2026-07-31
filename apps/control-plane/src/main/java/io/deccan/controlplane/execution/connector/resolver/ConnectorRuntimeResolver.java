package io.deccan.controlplane.execution.connector.resolver;

import io.deccan.controlplane.execution.connector.ConnectorRuntime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConnectorRuntimeResolver {

        private final List<ConnectorRuntime> runtimes;

        private Map<String, ConnectorRuntime> runtimeMap;

        public ConnectorRuntime resolve(
                String connector) {

                ConnectorRuntime runtime =
                        runtimeMap.get(
                                connector.toLowerCase());

                if(runtime == null){

                throw new IllegalArgumentException(

                        "No connector runtime registered for: "

                                + connector

                );

        }

return runtime;

        }
        @PostConstruct
        void initialize(){

        runtimeMap = new HashMap<>();

        for(ConnectorRuntime runtime : runtimes){

                String connector =
                        runtime.getClass()
                                .getSimpleName()
                                .replace("ConnectorRuntime","")
                                .toLowerCase();

                if(runtimeMap.containsKey(connector)){

                throw new IllegalStateException(

                        "Duplicate connector runtime: "

                                + connector

                );

                }

                runtimeMap.put(
                        connector,
                        runtime);

        }

        }

}