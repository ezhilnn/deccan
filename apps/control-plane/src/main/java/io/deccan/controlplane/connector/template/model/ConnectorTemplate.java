package io.deccan.controlplane.connector.template.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConnectorTemplate {

    private List<ConnectorField> fields =
            new ArrayList<>();

}