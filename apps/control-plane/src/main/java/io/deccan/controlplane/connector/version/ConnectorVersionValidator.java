package io.deccan.controlplane.connector.version;

import org.springframework.stereotype.Component;

@Component
public class ConnectorVersionValidator {

    public void validate(
            String version){

        if(version == null ||
                version.isBlank()){

            throw new IllegalArgumentException(
                    "Version is required");

        }

        if(!version.matches(
                "^\\d+\\.\\d+\\.\\d+$")){

            throw new IllegalArgumentException(
                    "Version must follow semantic versioning (x.y.z)");

        }

    }

}