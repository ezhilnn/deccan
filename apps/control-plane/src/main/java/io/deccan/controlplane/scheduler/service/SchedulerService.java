package io.deccan.controlplane.scheduler.service;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;

public interface SchedulerService {

    void initializeWorkflow(
            WorkflowExecution execution,
            WorkflowVersion version);

}