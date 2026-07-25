package io.deccan.controlplane.scheduler.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.scheduler.dto.request.CreateScheduleRequest;
import io.deccan.controlplane.scheduler.dto.response.WorkflowScheduleResponse;
import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
import io.deccan.controlplane.scheduler.mapper.WorkflowScheduleMapper;
import io.deccan.controlplane.scheduler.service.WorkflowScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workflows/{workflowId}/schedules")
public class WorkflowScheduleController {

    private final WorkflowScheduleService service;

    private final WorkflowScheduleMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('workflow.update')")
    public ApiResponse<WorkflowScheduleResponse> create(

            @PathVariable
            UUID workflowId,

            @Valid
            @RequestBody
            CreateScheduleRequest request) {

        WorkflowSchedule schedule =
                service.createSchedule(
                        workflowId,
                        mapper.toEntity(request));

        return ApiResponse.<WorkflowScheduleResponse>builder()
                .status(201)
                .message("Schedule created successfully")
                .data(
                        mapper.toResponse(schedule))
                .build();

    }

    @GetMapping
    @PreAuthorize("hasAuthority('workflow.read')")
    public ApiResponse<List<WorkflowScheduleResponse>> list(

            @PathVariable
            UUID workflowId) {

        List<WorkflowScheduleResponse> response =
                service.getSchedules(workflowId)
                        .stream()
                        .map(mapper::toResponse)
                        .toList();

        return ApiResponse.<List<WorkflowScheduleResponse>>builder()
                .status(200)
                .message("Schedules fetched successfully")
                .data(response)
                .build();

    }

}