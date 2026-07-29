package io.deccan.worker.connector;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConnectorRegistry {

    private final Map<String,Connector> connectors;

    public ConnectorRegistry(
            List<Connector> connectorList){

        connectors =
                connectorList.stream()
                        .collect(Collectors.toMap(
                                Connector::type,
                                Function.identity()));

    }

    public Connector get(
            String type){

        Connector connector =
                connectors.get(type);

        if(connector==null){

            throw new IllegalArgumentException(
                    "Unsupported connector : "+type);

        }

        return connector;

    }

}