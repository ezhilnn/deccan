package io.deccan.controlplane.workflow.mapper;

import io.deccan.controlplane.workflow.dto.response.WorkflowResponse;
import io.deccan.controlplane.workflow.entity.Workflow;
import org.springframework.stereotype.Component;

@Component
public class WorkflowMapper {

    public WorkflowResponse toResponse(Workflow workflow) {

        return WorkflowResponse.builder()
                .id(workflow.getId())
                .organizationId(workflow.getOrganization().getId())
                .name(workflow.getName())
                .description(workflow.getDescription())
                .status(workflow.getStatus())
                .currentVersion(workflow.getCurrentVersion())
                .createdAt(workflow.getCreatedAt())
                .updatedAt(workflow.getUpdatedAt())
                .build();

    }

}