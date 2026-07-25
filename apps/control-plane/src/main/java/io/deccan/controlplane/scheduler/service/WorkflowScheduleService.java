package io.deccan.controlplane.scheduler.service;

import java.util.List;
import java.util.UUID;

import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;

public interface WorkflowScheduleService {

    WorkflowSchedule createSchedule(
            UUID workflowId,
            WorkflowSchedule schedule);

    List<WorkflowSchedule> getSchedules(
            UUID workflowId);
    void enableSchedule(
        UUID scheduleId);

    void disableSchedule(
            UUID scheduleId);
    WorkflowSchedule updateSchedule(
        UUID scheduleId,
        WorkflowSchedule schedule);

    void deleteSchedule(
            UUID scheduleId);

}