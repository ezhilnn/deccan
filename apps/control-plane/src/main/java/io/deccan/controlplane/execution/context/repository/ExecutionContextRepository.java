package io.deccan.controlplane.execution.context.repository;

import io.deccan.controlplane.execution.context.entity.ExecutionContextEntity;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExecutionContextRepository
        extends JpaRepository<ExecutionContextEntity, UUID> {

    List<ExecutionContextEntity> findByExecution(
            WorkflowExecution execution);

    Optional<ExecutionContextEntity> findByExecutionAndNodeId(
            WorkflowExecution execution,
            String nodeId);

}