package io.deccan.controlplane.workflow.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.workflow.dto.request.CreateWorkflowRequest;
import io.deccan.controlplane.workflow.dto.request.PublishWorkflowRequest;
import io.deccan.controlplane.workflow.dto.request.UpdateWorkflowRequest;
import io.deccan.controlplane.workflow.dto.response.PageResponse;
import io.deccan.controlplane.workflow.dto.response.WorkflowExportResponse;
import io.deccan.controlplane.workflow.dto.response.WorkflowResponse;
import io.deccan.controlplane.workflow.dto.response.WorkflowVersionResponse;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import io.deccan.controlplane.workflow.mapper.WorkflowMapper;
import io.deccan.controlplane.workflow.mapper.WorkflowVersionMapper;
import io.deccan.controlplane.workflow.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.deccan.controlplane.workflow.dto.response.PageResponse;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

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
    @ResponseStatus(HttpStatus.CREATED)
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
        public ApiResponse<PageResponse<WorkflowResponse>> getWorkflows(

                @PathVariable UUID organizationId,

                @RequestParam(required = false)
                WorkflowStatus status,

                @RequestParam(defaultValue = "0")
                Integer page,

                @RequestParam(defaultValue = "20")
                Integer size) {

                Page<Workflow> workflows =
                        workflowService.getWorkflows(
                                organizationId,
                                status,
                                page,
                                size);

                PageResponse<WorkflowResponse> response =
                        PageResponse.<WorkflowResponse>builder()
                                .content(
                                        workflows.getContent()
                                                .stream()
                                                .map(mapper::toResponse)
                                                .toList())
                                .page(workflows.getNumber())
                                .size(workflows.getSize())
                                .totalElements(workflows.getTotalElements())
                                .totalPages(workflows.getTotalPages())
                                .build();

                return ApiResponse.<PageResponse<WorkflowResponse>>builder()
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
        @PreAuthorize("hasAuthority('workflow.write')")
        @PostMapping("/{workflowId}/archive")
        public ApiResponse<Void> archive(
                @PathVariable UUID workflowId){

        workflowService.archiveWorkflow(workflowId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Workflow archived successfully")
                .build();

        }
        @PreAuthorize("hasAuthority('workflow.write')")
        @PostMapping("/{workflowId}/activate")
        public ApiResponse<Void> activate(
                @PathVariable UUID workflowId){

        workflowService.activateWorkflow(workflowId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Workflow activated successfully")
                .build();

        }

                @PreAuthorize("hasAuthority('workflow.read')")
        @GetMapping("/{workflowId}")
        public ApiResponse<WorkflowResponse> getWorkflow(
                @PathVariable UUID workflowId) {

        WorkflowResponse response =
                mapper.toResponse(
                        workflowService.getWorkflow(workflowId));

        return ApiResponse.<WorkflowResponse>builder()
                .status(200)
                .message("Workflow fetched successfully")
                .data(response)
                .build();

        }
        @PreAuthorize("hasAuthority('workflow.write')")
        @PutMapping("/{workflowId}")
        public ApiResponse<WorkflowResponse> updateWorkflow(
                @PathVariable UUID workflowId,
                @Valid @RequestBody UpdateWorkflowRequest request) {

        WorkflowResponse response =
                mapper.toResponse(
                        workflowService.updateWorkflow(
                                workflowId,
                                request.getName(),
                                request.getDescription()));

        return ApiResponse.<WorkflowResponse>builder()
                .status(200)
                .message("Workflow updated successfully")
                .data(response)
                .build();

        }

        @PreAuthorize("hasAuthority('workflow.write')")
        @DeleteMapping("/{workflowId}")
        public ApiResponse<Void> deleteWorkflow(
                @PathVariable UUID workflowId) {

        workflowService.deleteWorkflow(workflowId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Workflow deleted successfully")
                .build();

        }

        @PreAuthorize("hasAuthority('workflow.read')")
        @GetMapping("/{workflowId}/export/{version}")
        public ApiResponse<WorkflowExportResponse> exportWorkflow(

                @PathVariable UUID workflowId,

                @PathVariable Integer version) {

        return ApiResponse.<WorkflowExportResponse>builder()
                .status(200)
                .message("Workflow exported successfully")
                .data(
                        workflowService.exportWorkflow(
                                workflowId,
                                version))
                .build();

        }
        @PreAuthorize("hasAuthority('workflow.write')")
        @PostMapping("/import/{organizationId}")
        public ApiResponse<WorkflowResponse> importWorkflow(

                @PathVariable UUID organizationId,

                @Valid
                @RequestBody WorkflowExportResponse request) {

        WorkflowResponse response =
                mapper.toResponse(

                        workflowService.importWorkflow(
                                organizationId,
                                request)

                );

        return ApiResponse.<WorkflowResponse>builder()
                .status(201)
                .message("Workflow imported successfully")
                .data(response)
                .build();

        }

}