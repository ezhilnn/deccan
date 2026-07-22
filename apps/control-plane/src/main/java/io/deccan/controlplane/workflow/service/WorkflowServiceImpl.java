package io.deccan.controlplane.workflow.service;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.identity.exception.IdentityNotFoundException;
import io.deccan.controlplane.identity.repository.OrganizationRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import io.deccan.controlplane.workflow.lifecycle.WorkflowLifecycleService;
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
import io.deccan.controlplane.workflow.dto.response.WorkflowExportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import io.deccan.controlplane.workflow.event.model.WorkflowEvent;
import io.deccan.controlplane.workflow.event.model.WorkflowEventType;
import io.deccan.controlplane.workflow.event.publisher.WorkflowEventPublisher;
import java.time.Instant;


@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowRepository workflowRepository;

    private final OrganizationRepository organizationRepository;
    private final WorkflowVersionRepository workflowVersionRepository;
    private final WorkflowValidator workflowValidator;
    private final WorkflowLifecycleService workflowLifecycleService;
    private final ObjectMapper objectMapper;
    private final WorkflowEventPublisher workflowEventPublisher;


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
       // Save first
        workflow = workflowRepository.save(workflow);

        // Now publish
        workflowEventPublisher.publish(
        WorkflowEvent.builder()
                .workflowId(workflow.getId())
                .organizationId(workflow.getOrganization().getId())
                .version(workflow.getCurrentVersion())
                .type(WorkflowEventType.CREATED)
                .timestamp(Instant.now())
                .build()
        );

        return workflow;

    }

        @Override
        @Transactional(readOnly = true)
        public Page<Workflow> getWorkflows(
                UUID organizationId,
                WorkflowStatus status,
                Integer page,
                Integer size) {

        Organization organization =
                organizationRepository.findById(organizationId)
                        .orElseThrow(() ->
                                new IdentityNotFoundException(
                                        "Organization not found"));

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        if (status == null) {

                return workflowRepository.findByOrganization(
                        organization,
                        pageable);

        }

        return workflowRepository.findByOrganizationAndStatus(
                organization,
                status,
                pageable);

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
        workflowLifecycleService.publish(workflow);

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
        // workflow.setStatus(
        //         WorkflowStatus.ACTIVE);

        workflowRepository.save(workflow);
        workflowEventPublisher.publish(

        WorkflowEvent.builder()
                .workflowId(
                        workflow.getId())
                .organizationId(
                        workflow.getOrganization().getId())
                .version(
                        version.getVersion())
                .type(
                        WorkflowEventType.PUBLISHED)
                .timestamp(
                        Instant.now())
                .build()

        );

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
        @Override
        public void archiveWorkflow(
                UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        workflowLifecycleService.archive(workflow);

        workflowRepository.save(workflow);
        workflowEventPublisher.publish(

        WorkflowEvent.builder()
                .workflowId(workflow.getId())
                .organizationId(
                        workflow.getOrganization().getId())
                .version(
                        workflow.getCurrentVersion())
                .type(
                        WorkflowEventType.ARCHIVED)
                .timestamp(
                        Instant.now())
                .build()

        );

        }

        @Override
        public void activateWorkflow(
                UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        workflowLifecycleService.activate(workflow);

        workflowRepository.save(workflow);
        workflowEventPublisher.publish(

        WorkflowEvent.builder()
                .workflowId(workflow.getId())
                .organizationId(
                        workflow.getOrganization().getId())
                .version(
                        workflow.getCurrentVersion())
                .type(
                        WorkflowEventType.ACTIVATED)
                .timestamp(
                        Instant.now())
                .build()

        );

        }

                @Override
        @Transactional(readOnly = true)
        public Workflow getWorkflow(UUID workflowId) {

        return workflowRepository.findById(workflowId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Workflow not found"));

        }

        @Override
        public Workflow updateWorkflow(
                UUID workflowId,
                String name,
                String description) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Workflow not found"));

        workflow.setName(name);
        workflow.setDescription(description);

        return workflowRepository.save(workflow);

        }

        @Override
        public void deleteWorkflow(
                UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Workflow not found"));

        if (workflow.getStatus() == WorkflowStatus.ACTIVE) {

                throw new IllegalArgumentException(
                        "Active workflow cannot be deleted");

        }

        workflowRepository.delete(workflow);

        }

        @Override
        @Transactional(readOnly = true)
        public WorkflowExportResponse exportWorkflow(
                UUID workflowId,
                Integer version) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Workflow not found"));

        WorkflowVersion workflowVersion =
                workflowVersionRepository
                        .findByWorkflowAndVersion(workflow, version)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Workflow version not found"));

        return WorkflowExportResponse.builder()
                .workflowId(workflow.getId())
                .workflowName(workflow.getName())
                .version(workflowVersion.getVersion())
                .definition(workflowVersion.getDefinition())
                .build();

        }

        @Override
        public Workflow importWorkflow(
                UUID organizationId,
                WorkflowExportResponse exported) {

        Workflow workflow =
                createWorkflow(
                        organizationId,
                        exported.getWorkflowName(),
                        "Imported Workflow"
                );

        publishWorkflow(
                workflow.getId(),
                exported.getDefinition()
        );

        return workflow;

        }

}