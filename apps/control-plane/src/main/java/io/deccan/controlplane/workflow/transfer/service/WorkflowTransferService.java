package io.deccan.controlplane.workflow.transfer.service;

import io.deccan.controlplane.workflow.transfer.dto.response.WorkflowExportResponse;

import java.util.UUID;

public interface WorkflowTransferService {

    WorkflowExportResponse exportWorkflow(
            UUID workflowId);

    UUID importWorkflow(
        UUID organizationId,
        WorkflowExportResponse request);

}