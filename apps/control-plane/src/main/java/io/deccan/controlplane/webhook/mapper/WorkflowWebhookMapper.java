package io.deccan.controlplane.webhook.mapper;

import io.deccan.controlplane.webhook.dto.response.WorkflowWebhookResponse;
import io.deccan.controlplane.webhook.entity.WorkflowWebhook;
import org.springframework.stereotype.Component;

@Component
public class WorkflowWebhookMapper {

    public WorkflowWebhookResponse toResponse(
            WorkflowWebhook webhook) {

        return WorkflowWebhookResponse.builder()
                .id(webhook.getId())
                .workflowId(
                        webhook.getWorkflow().getId())
                .token(
                        webhook.getToken())
                .enabled(
                        webhook.getEnabled())
                .createdAt(
                        webhook.getCreatedAt())
                .updatedAt(
                        webhook.getUpdatedAt())
                .build();

    }

}