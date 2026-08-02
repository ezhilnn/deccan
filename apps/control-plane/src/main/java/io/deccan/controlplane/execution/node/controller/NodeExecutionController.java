package io.deccan.controlplane.execution.node.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.node.dto.response.NodeExecutionResponse;
import io.deccan.controlplane.execution.node.mapper.NodeExecutionMapper;
import io.deccan.controlplane.execution.node.service.NodeExecutionService;
import io.deccan.controlplane.execution.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/node-executions")
public class NodeExecutionController {

    private final ExecutionService executionService;

    private final NodeExecutionService nodeExecutionService;

    private final NodeExecutionMapper mapper;

    @GetMapping("/executions/{executionId}")
    @PreAuthorize("hasAuthority('workflow.read')")
    public ApiResponse<List<NodeExecutionResponse>> list(

            @PathVariable
            UUID executionId) {

        WorkflowExecution execution =
                executionService.getExecution(
                        executionId);

        List<NodeExecutionResponse> response =
                nodeExecutionService.findAll(execution)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse.<List<NodeExecutionResponse>>builder()
                .status(200)
                .message("Node executions fetched successfully")
                .data(response)
                .build();

    }

}