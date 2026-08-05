package io.deccan.controlplane.worker.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.worker.dto.request.RegisterWorkerRequest;
import io.deccan.controlplane.worker.dto.request.WorkerShutdownRequest;
import io.deccan.controlplane.worker.dto.response.WorkerResponse;
import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.mapper.WorkerMapper;
import io.deccan.controlplane.worker.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import io.deccan.controlplane.worker.dto.response.HeartbeatResponse;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workers")
public class WorkerController {

    private final WorkerService service;

    private final WorkerMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkerResponse> register(

            @Valid
            @RequestBody
            RegisterWorkerRequest request){

        Worker worker =
                service.register(
                        mapper.toEntity(request));

        return ApiResponse.<WorkerResponse>builder()
                .status(201)
                .message("Worker registered successfully")
                .data(
                        mapper.toResponse(worker))
                .build();

    }

    @GetMapping("/{workerId}")
    public ApiResponse<WorkerResponse> get(

            @PathVariable
            UUID workerId){

        return ApiResponse.<WorkerResponse>builder()
                .status(200)
                .message("Worker fetched successfully")
                .data(
                        mapper.toResponse(
                                service.get(workerId)))
                .build();

    }

    @GetMapping
    public ApiResponse<List<WorkerResponse>> list(){

        List<WorkerResponse> workers =
                service.list()
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse.<List<WorkerResponse>>builder()
                .status(200)
                .message("Workers fetched successfully")
                .data(workers)
                .build();

    }
    @PostMapping("/{workerId}/heartbeat")
    public ApiResponse<HeartbeatResponse> heartbeat(

            @PathVariable
            UUID workerId){

        Worker worker =
                service.heartbeat(
                        workerId);

        return ApiResponse.<HeartbeatResponse>builder()
                .status(200)
                .message("Heartbeat received")
                .data(
                        HeartbeatResponse.builder()
                                .lastHeartbeat(
                                        worker.getLastHeartbeat())
                                .message(
                                        "Worker is online")
                                .build())
                .build();

    }
        @PostMapping("/{workerId}/shutdown")
        public ApiResponse<Void> shutdown(

                @PathVariable
                UUID workerId,

                @RequestBody
                WorkerShutdownRequest request){

        service.shutdown(
                workerId);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Worker shutdown acknowledged")
                .build();

        }
}