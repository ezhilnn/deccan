package io.deccan.controlplane.webhook.service;

import io.deccan.controlplane.webhook.dto.request.CreateWebhookRequest;
import io.deccan.controlplane.webhook.dto.request.UpdateWebhookRequest;
import io.deccan.controlplane.webhook.entity.Webhook;

import java.util.List;
import java.util.UUID;

public interface WebhookService {

    Webhook create(
            CreateWebhookRequest request);

    Webhook update(
            UUID webhookId,
            UpdateWebhookRequest request);

    Webhook get(
            UUID webhookId);

    List<Webhook> list(
            UUID workflowId);

    void delete(
            UUID webhookId);

    Webhook findByEndpoint(
            String endpoint);

}