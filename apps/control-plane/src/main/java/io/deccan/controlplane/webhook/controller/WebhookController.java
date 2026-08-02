package io.deccan.controlplane.webhook.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.execution.dto.request.ExecutionRequest;
import io.deccan.controlplane.execution.dto.response.ExecutionResponse;
import io.deccan.controlplane.execution.mapper.ExecutionMapper;
import io.deccan.controlplane.execution.service.ExecutionService;
import io.deccan.controlplane.webhook.dto.request.CreateWebhookRequest;
import io.deccan.controlplane.webhook.dto.request.UpdateWebhookRequest;
import io.deccan.controlplane.webhook.dto.response.WebhookResponse;
import io.deccan.controlplane.webhook.entity.Webhook;
import io.deccan.controlplane.webhook.mapper.WebhookMapper;
import io.deccan.controlplane.webhook.service.WebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks")
public class WebhookController {

    private final WebhookService service;

    private final WebhookMapper mapper;

    private final ExecutionService executionService;

    private final ExecutionMapper executionMapper;

    @PreAuthorize("hasAuthority('workflow.write')")
    @PostMapping
    public ApiResponse<WebhookResponse> create(

            @Valid
            @RequestBody
            CreateWebhookRequest request) {

        Webhook webhook =
                service.create(request);

        return ApiResponse.<WebhookResponse>builder()
                .status(201)
                .message("Webhook created successfully")
                .data(
                        mapper.toResponse(webhook))
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.read')")
    @GetMapping("/{webhookId}")
    public ApiResponse<WebhookResponse> get(

            @PathVariable
            UUID webhookId) {

        return ApiResponse.<WebhookResponse>builder()
                .status(200)
                .message("Webhook fetched successfully")
                .data(
                        mapper.toResponse(
                                service.get(webhookId)))
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.read')")
    @GetMapping("/workflows/{workflowId}")
    public ApiResponse<List<WebhookResponse>> list(

            @PathVariable
            UUID workflowId) {

        List<WebhookResponse> response =
                service.list(workflowId)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse.<List<WebhookResponse>>builder()
                .status(200)
                .message("Webhooks fetched successfully")
                .data(response)
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.write')")
    @PutMapping("/{webhookId}")
    public ApiResponse<WebhookResponse> update(

            @PathVariable
            UUID webhookId,

            @Valid
            @RequestBody
            UpdateWebhookRequest request) {

        Webhook webhook =
                service.update(
                        webhookId,
                        request);

        return ApiResponse.<WebhookResponse>builder()
                .status(200)
                .message("Webhook updated successfully")
                .data(
                        mapper.toResponse(webhook))
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.write')")
    @DeleteMapping("/{webhookId}")
    public ApiResponse<Void> delete(

            @PathVariable
            UUID webhookId) {

        service.delete(webhookId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Webhook deleted successfully")
                .build();

    }

    @PostMapping("/{endpoint}")
    public ApiResponse<ExecutionResponse> trigger(

            @PathVariable
            String endpoint,

            @RequestBody(required = false)
            ExecutionRequest request) {

        Webhook webhook =
                service.findByEndpoint(endpoint);

        ExecutionResponse response =
                executionMapper.toResponse(

                        executionService.executeWorkflow(

                                webhook.getWorkflow().getId(),

                                request == null
                                        ? null
                                        : request.getInput()

                        )

                );

        return ApiResponse.<ExecutionResponse>builder()
                .status(201)
                .message("Workflow triggered successfully")
                .data(response)
                .build();

    }

}