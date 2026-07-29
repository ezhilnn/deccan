package io.deccan.controlplane.scheduler.service;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;

public interface WorkflowSchedulerService {

    void initializeWorkflow(
            WorkflowExecution execution,
            WorkflowVersion version);
    void scheduleNextTasks(
        ExecutionTask completedTask);

}