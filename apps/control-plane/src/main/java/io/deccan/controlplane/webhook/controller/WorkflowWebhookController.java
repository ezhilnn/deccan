package io.deccan.controlplane.webhook.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.execution.dto.response.ExecutionResponse;
import io.deccan.controlplane.webhook.dto.response.WorkflowWebhookResponse;
import io.deccan.controlplane.webhook.entity.WorkflowWebhook;
import io.deccan.controlplane.webhook.mapper.WorkflowWebhookMapper;
import io.deccan.controlplane.webhook.service.WorkflowWebhookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WorkflowWebhookController {

    private final WorkflowWebhookService webhookService;

    private final WorkflowWebhookMapper mapper;

    @PostMapping("/workflows/{workflowId}/webhooks")
    @PreAuthorize("hasAuthority('workflow.update')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkflowWebhookResponse> register(

            @PathVariable
            UUID workflowId) {

        WorkflowWebhook webhook =
                webhookService.registerWebhook(
                        workflowId);

        return ApiResponse.<WorkflowWebhookResponse>builder()
                .status(201)
                .message("Webhook registered successfully")
                .data(
                        mapper.toResponse(webhook))
                .build();

    }

    @PostMapping("/webhooks/{token}")
    public ApiResponse<ExecutionResponse> execute(
            @PathVariable
            String token,

            @RequestBody(required = false)
            JsonNode payload) {

        ExecutionResponse execution =
        webhookService.executeWebhook(
                token,
                payload);

        return ApiResponse.<ExecutionResponse>builder()
                .status(200)
                .message("Webhook executed successfully")
                .data(execution)
                .build();

    }

}