package io.deccan.controlplane.webhook.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkflowWebhookResponse {

    private UUID id;

    private UUID workflowId;

    private String token;

    private Boolean enabled;

    private Instant  createdAt;

    private Instant  updatedAt;

}