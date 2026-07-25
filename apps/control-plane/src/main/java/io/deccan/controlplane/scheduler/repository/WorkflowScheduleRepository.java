package io.deccan.controlplane.scheduler.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
import io.deccan.controlplane.workflow.entity.Workflow;

public interface WorkflowScheduleRepository
        extends JpaRepository<WorkflowSchedule, UUID> {

    List<WorkflowSchedule> findByWorkflow(
            Workflow workflow);
    List<WorkflowSchedule> findByEnabledTrue();

}