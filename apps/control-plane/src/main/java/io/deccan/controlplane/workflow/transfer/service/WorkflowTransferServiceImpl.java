package io.deccan.controlplane.workflow.transfer.service;

import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import io.deccan.controlplane.workflow.repository.WorkflowVersionRepository;
import io.deccan.controlplane.workflow.transfer.dto.response.WorkflowExportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.identity.entity.Organization;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowTransferServiceImpl
        implements WorkflowTransferService {

    private final WorkflowRepository workflowRepository;

    private final WorkflowVersionRepository workflowVersionRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional(readOnly = true)
    public WorkflowExportResponse exportWorkflow(
            UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        WorkflowVersion version =
                workflowVersionRepository
                        .findByWorkflowAndVersion(
                                workflow,
                                workflow.getCurrentVersion())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow version not found"));

        return WorkflowExportResponse.builder()
                .name(workflow.getName())
                .description(workflow.getDescription())
                .status(workflow.getStatus())
                .version(version.getVersion())
                .definition(version.getDefinition())
                .build();

    }

    @Override
   public UUID importWorkflow(
        UUID organizationId,
        WorkflowExportResponse request) {

        Workflow workflow =
                new Workflow();
        Organization organization =
        organizationRepository
                .findById(organizationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Organization not found"));

        workflow.setOrganization(
                organization);

        workflow.setName(
                request.getName());

        workflow.setDescription(
                request.getDescription());

        workflow.setStatus(
                request.getStatus());

        workflow.setCurrentVersion(
                request.getVersion());
        if (workflowRepository.existsByOrganizationAndName(
                organization,
                request.getName())) {

        throw new IllegalArgumentException(
                "Workflow already exists");

        }
       

        workflow =
                workflowRepository.save(
                        workflow);

        WorkflowVersion version =
                new WorkflowVersion();

        version.setWorkflow(
                workflow);

        version.setVersion(
                request.getVersion());

        version.setDefinition(
                request.getDefinition());

        version.setPublished(
                true);

        workflowVersionRepository.save(
                version);

        return workflow.getId();

    }

}