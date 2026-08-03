package io.deccan.controlplane.task.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.task.dto.request.TaskHeartbeatRequest;
import io.deccan.controlplane.task.dto.request.TaskResultRequest;
import io.deccan.controlplane.task.dto.response.ExecutionTaskResponse;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.mapper.ExecutionTaskMapper;
import io.deccan.controlplane.task.service.ExecutionTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


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
        taskId,
        request.getOutput());

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
    public ResponseEntity<ApiResponse<ExecutionTaskResponse>> leaseTask(){

        ExecutionTask task =
                service.leaseNextTask();
        if (task == null) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(
        ApiResponse.<ExecutionTaskResponse>builder()
                .status(200)
                .message("Task leased successfully")
                .data(mapper.toResponse(task))
                        .build()
        );

    }
        @PostMapping("/{taskId}/heartbeat")
        @PreAuthorize("hasAuthority('workflow.execute')")
        public ApiResponse<Void> heartbeat(

                @PathVariable
                UUID taskId,

                @RequestBody
                TaskHeartbeatRequest request){

        service.heartbeat(

                taskId,

                request.getExtendBySeconds());

        return ApiResponse.<Void>builder()

                .status(200)

                .message("Heartbeat received")

                .build();

        }

}