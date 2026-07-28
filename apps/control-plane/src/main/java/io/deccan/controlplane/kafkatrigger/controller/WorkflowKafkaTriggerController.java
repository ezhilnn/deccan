package io.deccan.controlplane.kafkatrigger.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.kafkatrigger.dto.response.WorkflowKafkaTriggerResponse;
import io.deccan.controlplane.kafkatrigger.entity.WorkflowKafkaTrigger;
import io.deccan.controlplane.kafkatrigger.mapper.WorkflowKafkaTriggerMapper;
import io.deccan.controlplane.kafkatrigger.registry.KafkaTriggerRegistry;
import io.deccan.controlplane.kafkatrigger.service.WorkflowKafkaTriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflows/{workflowId}/kafka-triggers")
public class WorkflowKafkaTriggerController {

    private final WorkflowKafkaTriggerService service;

    private final WorkflowKafkaTriggerMapper mapper;
    private final KafkaTriggerRegistry registry;

    @PostMapping
    @PreAuthorize("hasAuthority('workflow.update')")
    public ApiResponse<WorkflowKafkaTriggerResponse> register(

            @PathVariable
            UUID workflowId,

            @RequestBody
            Map<String, String> request) {

        WorkflowKafkaTrigger trigger =
        service.registerTrigger(
                workflowId,
                request.get("topic"));

        registry.register(trigger);

        return ApiResponse.<WorkflowKafkaTriggerResponse>builder()
                .status(201)
                .message("Kafka trigger registered successfully")
                .data(mapper.toResponse(trigger))
                .build();

    }
    @PostMapping("/{triggerId}/disable")
    @PreAuthorize("hasAuthority('workflow.update')")
    public ApiResponse<Void> disable(

            @PathVariable
            UUID workflowId,

            @PathVariable
            UUID triggerId){

        WorkflowKafkaTrigger trigger =
        service.disableTrigger(triggerId);

        registry.unregister(trigger.getTopic());

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Kafka trigger disabled successfully")
                .build();

    }
    @DeleteMapping("/{triggerId}")
    @PreAuthorize("hasAuthority('workflow.update')")
    public ApiResponse<Void> delete(

            @PathVariable
            UUID workflowId,

            @PathVariable
            UUID triggerId){

        WorkflowKafkaTrigger trigger =
        service.deleteTrigger(triggerId);

        registry.unregister(trigger.getTopic());

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Kafka trigger deleted successfully")
                .build();

    }

}