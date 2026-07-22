package io.deccan.controlplane.execution.repository;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.workflow.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowExecutionRepository
        extends JpaRepository<WorkflowExecution,UUID>{

    List<WorkflowExecution>
    findByWorkflowOrderByStartedAtDesc(
            Workflow workflow);

    List<WorkflowExecution>
    findByStatus(
            ExecutionStatus status);
    boolean existsByWorkflowAndStatus(
        Workflow workflow,
        ExecutionStatus status);

}