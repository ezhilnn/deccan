package io.deccan.controlplane.execution.node.repository;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.node.entity.NodeExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NodeExecutionRepository
        extends JpaRepository<NodeExecution, UUID> {

    List<NodeExecution> findByWorkflowExecutionOrderByStartedAt(
            WorkflowExecution execution);

}