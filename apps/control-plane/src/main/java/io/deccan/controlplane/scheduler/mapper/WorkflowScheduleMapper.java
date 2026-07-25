package io.deccan.controlplane.scheduler.mapper;

import io.deccan.controlplane.scheduler.dto.request.CreateScheduleRequest;
import io.deccan.controlplane.scheduler.dto.response.WorkflowScheduleResponse;
import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
import org.springframework.stereotype.Component;

@Component
public class WorkflowScheduleMapper {

    public WorkflowSchedule toEntity(
            CreateScheduleRequest request) {

        WorkflowSchedule schedule =
                new WorkflowSchedule();

        schedule.setType(
                request.getType());

        schedule.setCronExpression(
                request.getCronExpression());

        schedule.setEnabled(
                request.getEnabled());

        return schedule;

    }

    public WorkflowScheduleResponse toResponse(
            WorkflowSchedule schedule) {

        return WorkflowScheduleResponse.builder()
                .id(schedule.getId())
                .workflowId(
                        schedule.getWorkflow().getId())
                .type(schedule.getType())
                .cronExpression(
                        schedule.getCronExpression())
                .enabled(schedule.getEnabled())
                .createdAt(
                        schedule.getCreatedAt())
                .updatedAt(
                        schedule.getUpdatedAt())
                .build();

    }

}