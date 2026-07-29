package io.deccan.controlplane.execution.graph;

import io.deccan.controlplane.workflow.entity.WorkflowVersion;

public interface WorkflowGraphService {

    ExecutionWorkflowGraph buildGraph(
            WorkflowVersion version);

}