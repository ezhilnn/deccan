package io.deccan.controlplane.connector.validation;

import io.deccan.controlplane.connector.entity.Connector;
import org.springframework.stereotype.Component;

@Component
public class ConnectorValidator {

    public void validate(
            Connector connector){

        if(connector.getName()==null ||
                connector.getName().isBlank()){

            throw new IllegalArgumentException(
                    "Connector name is required");

        }

        if(connector.getDisplayName()==null ||
                connector.getDisplayName().isBlank()){

            throw new IllegalArgumentException(
                    "Connector display name is required");

        }

        if(connector.getConfigurationSchema()==null){

            throw new IllegalArgumentException(
                    "Configuration schema is required");

        }

    }

}