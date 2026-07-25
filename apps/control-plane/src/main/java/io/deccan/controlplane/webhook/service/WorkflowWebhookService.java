package io.deccan.controlplane.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.webhook.entity.WorkflowWebhook;

import java.util.UUID;

public interface WorkflowWebhookService {

    WorkflowWebhook registerWebhook(
            UUID workflowId);

    WorkflowExecution executeWebhook(
            String token,
            JsonNode payload);

}