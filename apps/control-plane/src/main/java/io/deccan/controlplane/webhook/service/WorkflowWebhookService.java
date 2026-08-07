package io.deccan.controlplane.webhook.service;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import io.deccan.controlplane.execution.dto.response.ExecutionResponse;
import io.deccan.controlplane.webhook.entity.WorkflowWebhook;

public interface WorkflowWebhookService {

    WorkflowWebhook registerWebhook(
            UUID workflowId);

    ExecutionResponse executeWebhook(
        String token,
        JsonNode payload);

}