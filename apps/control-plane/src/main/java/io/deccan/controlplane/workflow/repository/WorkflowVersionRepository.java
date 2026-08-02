package io.deccan.controlplane.workflow.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;

public interface WorkflowVersionRepository
        extends JpaRepository<WorkflowVersion, UUID> {

    List<WorkflowVersion> findByWorkflowOrderByVersionDesc(
            Workflow workflow
    );

    Optional<WorkflowVersion> findByWorkflowAndVersion(
            Workflow workflow,
            Integer version
    );

    Optional<WorkflowVersion> findFirstByWorkflowOrderByVersionDesc(
            Workflow workflow
    );
    Optional<WorkflowVersion> findByWorkflowIdAndVersion(
        UUID workflowId,
        Integer version);
}