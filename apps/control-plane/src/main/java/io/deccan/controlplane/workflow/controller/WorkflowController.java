package io.deccan.controlplane.workflow.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.workflow.dto.request.CreateWorkflowRequest;
import io.deccan.controlplane.workflow.dto.request.PublishWorkflowRequest;
import io.deccan.controlplane.workflow.dto.response.WorkflowResponse;
import io.deccan.controlplane.workflow.dto.response.WorkflowVersionResponse;
import io.deccan.controlplane.workflow.mapper.WorkflowMapper;
import io.deccan.controlplane.workflow.mapper.WorkflowVersionMapper;
import io.deccan.controlplane.workflow.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;
    private final WorkflowVersionMapper versionMapper;

    private final WorkflowMapper mapper;

    @PreAuthorize("hasAuthority('workflow.write')")
    @PostMapping
    public ApiResponse<WorkflowResponse> createWorkflow(
            @Valid @RequestBody CreateWorkflowRequest request) {

        WorkflowResponse response =
                mapper.toResponse(

                        workflowService.createWorkflow(
                                request.getOrganizationId(),
                                request.getName(),
                                request.getDescription()
                        )

                );

        return ApiResponse.<WorkflowResponse>builder()
                .status(201)
                .message("Workflow created successfully")
                .data(response)
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.read')")
    @GetMapping("/organizations/{organizationId}")
    public ApiResponse<List<WorkflowResponse>> getWorkflows(
            @PathVariable UUID organizationId) {

        List<WorkflowResponse> response =
                workflowService.getWorkflows(organizationId)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse.<List<WorkflowResponse>>builder()
                .status(200)
                .message("Workflows fetched successfully")
                .data(response)
                .build();

    }
    @PreAuthorize("hasAuthority('workflow.write')")
    @PostMapping("/{workflowId}/publish")
    public ApiResponse<WorkflowVersionResponse> publishWorkflow(

            @PathVariable UUID workflowId,

            @Valid
            @RequestBody PublishWorkflowRequest request) {

        WorkflowVersionResponse response =
                versionMapper.toResponse(

                        workflowService.publishWorkflow(

                                workflowId,

                                request.getDefinition()

                        )

                );

        return ApiResponse
                .<WorkflowVersionResponse>builder()
                .status(201)
                .message("Workflow published successfully")
                .data(response)
                .build();

    }
    @PreAuthorize("hasAuthority('workflow.read')")
    @GetMapping("/{workflowId}/versions")
    public ApiResponse<List<WorkflowVersionResponse>> versions(

            @PathVariable UUID workflowId) {

        List<WorkflowVersionResponse> response =
                workflowService
                        .getWorkflowVersions(workflowId)
                        .stream()
                        .map(versionMapper::toResponse)
                        .toList();

        return ApiResponse
                .<List<WorkflowVersionResponse>>builder()
                .status(200)
                .message("Workflow versions fetched successfully")
                .data(response)
                .build();

    }

}