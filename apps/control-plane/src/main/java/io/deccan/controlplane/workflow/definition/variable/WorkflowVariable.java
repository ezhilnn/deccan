package io.deccan.controlplane.workflow.definition.variable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowVariable {

    private String name;

    private String type;

    private Object defaultValue;

}