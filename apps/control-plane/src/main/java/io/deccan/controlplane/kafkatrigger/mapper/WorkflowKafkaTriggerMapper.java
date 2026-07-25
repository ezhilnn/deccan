package io.deccan.controlplane.kafkatrigger.mapper;

import io.deccan.controlplane.kafkatrigger.dto.response.WorkflowKafkaTriggerResponse;
import io.deccan.controlplane.kafkatrigger.entity.WorkflowKafkaTrigger;
import org.springframework.stereotype.Component;

@Component
public class WorkflowKafkaTriggerMapper {

    public WorkflowKafkaTriggerResponse toResponse(
            WorkflowKafkaTrigger trigger) {

        return WorkflowKafkaTriggerResponse.builder()
                .id(trigger.getId())
                .workflowId(trigger.getWorkflow().getId())
                .topic(trigger.getTopic())
                .enabled(trigger.getEnabled())
                .createdAt(trigger.getCreatedAt())
                .updatedAt(trigger.getUpdatedAt())
                .build();

    }

}