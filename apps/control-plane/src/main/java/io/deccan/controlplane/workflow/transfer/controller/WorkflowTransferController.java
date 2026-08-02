package io.deccan.controlplane.workflow.transfer.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.workflow.transfer.dto.response.WorkflowExportResponse;
import io.deccan.controlplane.workflow.transfer.service.WorkflowTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('workflow.write')")
    public ApiResponse<UUID> importWorkflow(

            @RequestBody
            WorkflowExportResponse request) {

        UUID workflowId =
                service.importWorkflow(
                        request);

        return ApiResponse.<UUID>builder()
                .status(201)
                .message("Workflow imported successfully")
                .data(workflowId)
                .build();

    }

}