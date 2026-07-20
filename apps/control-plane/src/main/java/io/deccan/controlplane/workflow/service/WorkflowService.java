package io.deccan.controlplane.workflow.service;

import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;

import java.util.List;
import java.util.UUID;

public interface WorkflowService {

    Workflow createWorkflow(
            UUID organizationId,
            String name,
            String description
    );

    List<Workflow> getWorkflows(
            UUID organizationId
    );

    WorkflowVersion publishWorkflow(
            UUID workflowId,
            String definition
    );

    List<WorkflowVersion> getWorkflowVersions(
            UUID workflowId
    );

}