package io.deccan.controlplane.webhook.service;

import io.deccan.controlplane.webhook.dto.request.CreateWebhookRequest;
import io.deccan.controlplane.webhook.dto.request.UpdateWebhookRequest;
import io.deccan.controlplane.webhook.entity.Webhook;
import io.deccan.controlplane.webhook.repository.WebhookRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WebhookServiceImpl
        implements WebhookService {

    private final WebhookRepository repository;

    private final WorkflowRepository workflowRepository;

    @Override
    public Webhook create(
            CreateWebhookRequest request) {

        if (repository.existsByEndpoint(
                request.getEndpoint())) {

            throw new IllegalArgumentException(
                    "Webhook endpoint already exists");

        }

        Workflow workflow =
                workflowRepository.findById(
                        request.getWorkflowId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        Webhook webhook =
                new Webhook();

        webhook.setWorkflow(workflow);
        webhook.setEndpoint(
                request.getEndpoint());
        webhook.setSecret(
                request.getSecret());
        webhook.setEnabled(true);

        return repository.save(
                webhook);

    }

    @Override
    public Webhook update(
            UUID webhookId,
            UpdateWebhookRequest request) {

        Webhook webhook =
                repository.findById(webhookId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Webhook not found"));

        webhook.setEndpoint(
                request.getEndpoint());

        webhook.setSecret(
                request.getSecret());

        webhook.setEnabled(
                request.getEnabled());

        return repository.save(
                webhook);

    }

    @Override
    @Transactional(readOnly = true)
    public Webhook get(
            UUID webhookId) {

        return repository.findById(
                webhookId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Webhook not found"));

    }

    @Override
    @Transactional(readOnly = true)
    public List<Webhook> list(
            UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(
                        workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        return repository.findByWorkflow(
                workflow);

    }

    @Override
    public void delete(
            UUID webhookId) {

        repository.deleteById(
                webhookId);

    }

    @Override
    @Transactional(readOnly = true)
    public Webhook findByEndpoint(
            String endpoint) {

        return repository.findByEndpoint(
                endpoint)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Webhook not found"));

    }
    @Override
    public void validateWebhook(
            Webhook webhook,
            String secret){

        if(Boolean.FALSE.equals(
                webhook.getEnabled())){

            throw new IllegalStateException(
                    "Webhook is disabled");

        }

        if(!webhook.getSecret().equals(secret)){

            throw new IllegalArgumentException(
                    "Invalid webhook secret");

        }

    }

}