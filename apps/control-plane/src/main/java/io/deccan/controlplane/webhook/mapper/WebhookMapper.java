package io.deccan.controlplane.webhook.mapper;

import io.deccan.controlplane.webhook.dto.response.WebhookResponse;
import io.deccan.controlplane.webhook.entity.Webhook;
import org.springframework.stereotype.Component;

@Component
public class WebhookMapper {

    public WebhookResponse toResponse(
            Webhook webhook) {

        return WebhookResponse.builder()
                .id(webhook.getId())
                .workflowId(
                        webhook.getWorkflow().getId())
                .endpoint(
                        webhook.getEndpoint())
                .enabled(
                        webhook.getEnabled())
                .createdAt(
                        webhook.getCreatedAt())
                .updatedAt(
                        webhook.getUpdatedAt())
                .build();

    }

}