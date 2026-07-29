package io.deccan.controlplane.task.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.task.dto.request.TaskResultRequest;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.service.ExecutionTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.deccan.controlplane.task.dto.response.ExecutionTaskResponse;
import io.deccan.controlplane.task.mapper.ExecutionTaskMapper;

import java.util.UUID;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class ExecutionTaskController {

    private final ExecutionTaskService service;
    private final ExecutionTaskMapper mapper;

    @PostMapping("/{taskId}/result")
    @PreAuthorize("hasAuthority('workflow.execute')")
    public ApiResponse<Void> reportResult(

            @PathVariable
            UUID taskId,

            @Valid
            @RequestBody
            TaskResultRequest request){

        if(request.isSuccess()){

            service.reportSuccess(
                    taskId);

        }
        else{

            service.reportFailure(

                    taskId,

                    request.getErrorMessage()

            );

        }

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Task result accepted")
                .build();

    }
    @GetMapping("/executions/{executionId}")
    @PreAuthorize("hasAuthority('workflow.read')")
    public ApiResponse<List<ExecutionTaskResponse>> getTasks(

            @PathVariable
            UUID executionId){

        List<ExecutionTaskResponse> response =
        service.getTasks(executionId)
               .stream()
               .map(mapper::toResponse)
               .toList();

                return ApiResponse.<List<ExecutionTaskResponse>>builder()
                        .status(200)
                        .message("Execution tasks fetched")
                        .data(response)
                        .build();

    }
    @PostMapping("/lease")
    public ApiResponse<ExecutionTaskResponse> leaseTask(){

        ExecutionTask task =
                service.leaseNextTask();

        return ApiResponse.<ExecutionTaskResponse>builder()

                .status(200)

                .message("Task leased successfully")

                .data(
                        mapper.toResponse(task))

                .build();

    }

}