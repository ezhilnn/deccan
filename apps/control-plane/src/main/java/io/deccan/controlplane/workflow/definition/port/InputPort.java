package io.deccan.controlplane.workflow.definition.port;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputPort {

    private String name;

    private String type;

    private boolean required;

}