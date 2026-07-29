package io.deccan.controlplane.scheduler.service;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl
        implements SchedulerService {

    @Override
    public void initializeWorkflow(
            WorkflowExecution execution,
            WorkflowVersion version) {

        // Milestone 13.3

    }

}