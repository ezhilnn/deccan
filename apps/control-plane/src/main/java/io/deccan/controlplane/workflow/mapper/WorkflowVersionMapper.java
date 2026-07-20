package io.deccan.controlplane.workflow.mapper;

import io.deccan.controlplane.workflow.dto.response.WorkflowVersionResponse;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import org.springframework.stereotype.Component;

@Component
public class WorkflowVersionMapper {

    public WorkflowVersionResponse toResponse(
            WorkflowVersion version) {

        return WorkflowVersionResponse.builder()
                .id(version.getId())
                .version(version.getVersion())
                .definition(version.getDefinition())
                .published(version.getPublished())
                .createdAt(version.getCreatedAt())
                .build();

    }

}