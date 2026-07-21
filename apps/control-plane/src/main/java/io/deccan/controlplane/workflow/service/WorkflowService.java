package io.deccan.controlplane.workflow.service;

import io.deccan.controlplane.workflow.dto.response.WorkflowExportResponse;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;

public interface WorkflowService {

        Workflow createWorkflow(
                UUID organizationId,
                String name,
                String description
        );

        Page<Workflow> getWorkflows(
                UUID organizationId,
                WorkflowStatus status,
                Integer page,
                Integer size
        );
        
        WorkflowVersion publishWorkflow(
                UUID workflowId,
                JsonNode definition
        );

        List<WorkflowVersion> getWorkflowVersions(
                UUID workflowId
        );
        void archiveWorkflow(
                UUID workflowId);

        void activateWorkflow(
                        UUID workflowId);
        Workflow updateWorkflow(
                UUID workflowId,
                String name,
                String description
        );

        void deleteWorkflow(
                UUID workflowId
        );

        Workflow getWorkflow(
                UUID workflowId
        );

        WorkflowExportResponse exportWorkflow(
        UUID workflowId,
        Integer version);

        Workflow importWorkflow(
                UUID organizationId,
                WorkflowExportResponse workflow);

}