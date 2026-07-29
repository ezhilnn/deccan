package io.deccan.controlplane.execution.graph;

import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ExecutionWorkflowGraph {

    private Map<String, WorkflowNode> nodes;

    private Map<String, List<WorkflowEdge>> outgoingEdges;

    private Map<String, List<WorkflowEdge>> incomingEdges;

}