package io.deccan.controlplane.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.service.ExecutionService;
import io.deccan.controlplane.webhook.entity.WorkflowWebhook;
import io.deccan.controlplane.webhook.repository.WorkflowWebhookRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowWebhookServiceImpl
        implements WorkflowWebhookService {

    private final WorkflowRepository workflowRepository;

    private final WorkflowWebhookRepository webhookRepository;

    private final ExecutionService executionService;

    @Override
    public WorkflowWebhook registerWebhook(
            UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        WorkflowWebhook webhook =
                new WorkflowWebhook();

        webhook.setWorkflow(workflow);

        webhook.setToken(
                UUID.randomUUID().toString());

        webhook.setEnabled(true);

        return webhookRepository.save(webhook);

    }

    @Override
    public WorkflowExecution executeWebhook(
            String token,
            JsonNode payload) {

        WorkflowWebhook webhook =
                webhookRepository.findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Webhook not found"));

        if (!Boolean.TRUE.equals(
                webhook.getEnabled())) {

            throw new IllegalArgumentException(
                    "Webhook is disabled");

        }

        return executionService.executeWorkflow(
                webhook.getWorkflow().getId(),
                payload);

    }

}