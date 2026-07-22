package io.deccan.controlplane.execution.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.execution.dto.request.ExecutionRequest;
import io.deccan.controlplane.execution.dto.response.ExecutionResponse;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.mapper.ExecutionMapper;
import io.deccan.controlplane.execution.service.ExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/executions")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    private final ExecutionMapper mapper;

    @PreAuthorize("hasAuthority('workflow.execute')")
    @PostMapping("/workflows/{workflowId}")
    public ApiResponse<ExecutionResponse> execute(

            @PathVariable
            UUID workflowId,

            @Valid
            @RequestBody
            ExecutionRequest request){

        ExecutionResponse response =
                mapper.toResponse(

                        executionService.executeWorkflow(

                                workflowId,

                                request.getInput()

                        )

                );

        return ApiResponse
                .<ExecutionResponse>builder()
                .status(201)
                .message("Workflow execution started")
                .data(response)
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.read')")
    @GetMapping("/{executionId}")
    public ApiResponse<ExecutionResponse> get(

            @PathVariable
            UUID executionId){

        return ApiResponse
                .<ExecutionResponse>builder()
                .status(200)
                .message("Execution fetched successfully")
                .data(
                        mapper.toResponse(
                                executionService.getExecution(
                                        executionId
                                )
                        )
                )
                .build();

    }

    @PreAuthorize("hasAuthority('workflow.read')")
    @GetMapping("/workflows/{workflowId}")
    public ApiResponse<List<ExecutionResponse>> list(

            @PathVariable
            UUID workflowId){

        List<ExecutionResponse> response =
                executionService.getExecutions(
                                workflowId)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse
                .<List<ExecutionResponse>>builder()
                .status(200)
                .message("Executions fetched successfully")
                .data(response)
                .build();


    }
    @PreAuthorize("hasAuthority('workflow.execute')")
        @PostMapping("/{executionId}/cancel")
        public ApiResponse<Void> cancel(

                @PathVariable
                UUID executionId){

                executionService.cancelExecution(
                        executionId);

                return ApiResponse.<Void>builder()
                        .status(200)
                        .message("Execution cancelled successfully")
                        .build();


        }

        @PreAuthorize("hasAuthority('workflow.execute')")
        @PostMapping("/{executionId}/retry")
        public ApiResponse<ExecutionResponse> retry(

                @PathVariable
                UUID executionId){

                WorkflowExecution execution =
                        executionService.retryExecution(
                                executionId);

                return ApiResponse.<ExecutionResponse>builder()
                        .status(201)
                        .message("Execution retried successfully")
                        .data(
                                mapper.toResponse(
                                        execution))
                        .build();

        }

}