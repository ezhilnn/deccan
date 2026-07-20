package io.deccan.controlplane.workflow.definition;

import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.definition.trigger.WorkflowTrigger;
import io.deccan.controlplane.workflow.definition.variable.WorkflowVariable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WorkflowDefinition {

    private Integer schemaVersion = 1;

    private WorkflowTrigger trigger;

    private List<WorkflowNode> nodes = new ArrayList<>();

    private List<WorkflowEdge> edges = new ArrayList<>();

    private List<WorkflowVariable> variables = new ArrayList<>();

}