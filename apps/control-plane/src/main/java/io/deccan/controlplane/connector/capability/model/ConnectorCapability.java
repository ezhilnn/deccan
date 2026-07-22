package io.deccan.controlplane.connector.capability.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectorCapability {

    /**
     * Example:
     * execute
     * trigger
     * stream
     * upload
     */
    private String name;

    /**
     * Human readable description.
     */
    private String description;

}