package io.deccan.controlplane.workflow.transfer.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.workflow.transfer.dto.response.WorkflowExportResponse;
import io.deccan.controlplane.workflow.transfer.service.WorkflowTransferService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow-transfer")
public class WorkflowTransferController {

    private final WorkflowTransferService service;

    @GetMapping("/{workflowId}/export")
    @PreAuthorize("hasAuthority('workflow.read')")
    public ApiResponse<WorkflowExportResponse> exportWorkflow(

            @PathVariable
            UUID workflowId) {

        return ApiResponse.<WorkflowExportResponse>builder()
                .status(200)
                .message("Workflow exported successfully")
                .data(
                        service.exportWorkflow(
                                workflowId))
                .build();

    }

    @PostMapping("/import/{organizationId}")
    @PreAuthorize("hasAuthority('workflow.write')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UUID> importWorkflow(

        @PathVariable
        UUID organizationId,

        @RequestBody
        WorkflowExportResponse request) {

        UUID workflowId =
        service.importWorkflow(
                organizationId,
                request);

        return ApiResponse.<UUID>builder()
                .status(201)
                .message("Workflow imported successfully")
                .data(workflowId)
                .build();

    }

}