package io.deccan.controlplane.execution.engine.node;

import io.deccan.controlplane.workflow.definition.node.WorkflowNode;

public interface NodeExecutor {

    boolean supports(
            WorkflowNode node);

    void execute(
            WorkflowNode node);

}