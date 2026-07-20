package io.deccan.controlplane.workflow.service;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.exception.IdentityNotFoundException;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import io.deccan.controlplane.workflow.repository.WorkflowVersionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.validation.WorkflowValidator;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowRepository workflowRepository;

    private final OrganizationRepository organizationRepository;
    private final WorkflowVersionRepository workflowVersionRepository;
    private final WorkflowValidator workflowValidator;

    private final ObjectMapper objectMapper;

    @Override
    public Workflow createWorkflow(
            UUID organizationId,
            String name,
            String description) {

        Organization organization =
                organizationRepository.findById(organizationId)
                        .orElseThrow(() ->
                                new IdentityNotFoundException("Organization not found"));

        if (workflowRepository.existsByOrganizationAndName(
                organization,
                name)) {

            throw new IllegalArgumentException(
                    "Workflow already exists");

        }

        Workflow workflow = new Workflow();

        workflow.setOrganization(organization);
        workflow.setName(name);
        workflow.setDescription(description);
        workflow.setStatus(WorkflowStatus.DRAFT);
        workflow.setCurrentVersion(1);

        return workflowRepository.save(workflow);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Workflow> getWorkflows(
            UUID organizationId) {

        Organization organization =
                organizationRepository.findById(organizationId)
                        .orElseThrow(() ->
                                new IdentityNotFoundException("Organization not found"));

        return workflowRepository.findByOrganization(
                organization);

    }
    @Override
public WorkflowVersion publishWorkflow(
        UUID workflowId,
        JsonNode definition){
    Workflow workflow =
            workflowRepository.findById(workflowId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Workflow not found"));

    Integer nextVersion =
            workflowVersionRepository
                    .findFirstByWorkflowOrderByVersionDesc(workflow)
                    .map(v -> v.getVersion() + 1)
                    .orElse(1);

    WorkflowVersion version =
            new WorkflowVersion();
   WorkflowDefinition workflowDefinition;

    try {

        workflowDefinition =
        objectMapper.treeToValue(
                definition,
                WorkflowDefinition.class);


    } catch (Exception ex) {

        throw new IllegalArgumentException(
                "Invalid workflow definition",
                ex
        );

    }
      workflowValidator.validate(workflowDefinition);

        version.setWorkflow(workflow);
        version.setVersion(nextVersion);

        version.setDefinition(
                objectMapper.valueToTree(workflowDefinition)
        );

        version.setPublished(true);

    version =
            workflowVersionRepository.save(version);

    workflow.setCurrentVersion(nextVersion);
    workflow.setStatus(WorkflowStatus.ACTIVE);

    workflowRepository.save(workflow);

    return version;

}

@Override
@Transactional(readOnly = true)
public List<WorkflowVersion> getWorkflowVersions(
        UUID workflowId) {

    Workflow workflow =
            workflowRepository.findById(workflowId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Workflow not found"));

    return workflowVersionRepository
            .findByWorkflowOrderByVersionDesc(workflow);

}

}