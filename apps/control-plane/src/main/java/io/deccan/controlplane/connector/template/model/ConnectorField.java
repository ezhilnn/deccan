package io.deccan.controlplane.connector.template.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectorField {

    private String name;

    private String label;

    private String type;

    private Boolean required;

    private String defaultValue;

    private Integer order;

}