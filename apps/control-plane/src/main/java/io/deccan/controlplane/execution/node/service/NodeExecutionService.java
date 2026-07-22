package io.deccan.controlplane.execution.node.service;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.node.entity.NodeExecution;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;

public interface NodeExecutionService {

    NodeExecution start(
            WorkflowExecution execution,
            WorkflowNode node);

    void complete(
            NodeExecution nodeExecution,
            Object output);

    void fail(
            NodeExecution nodeExecution,
            String error);

}