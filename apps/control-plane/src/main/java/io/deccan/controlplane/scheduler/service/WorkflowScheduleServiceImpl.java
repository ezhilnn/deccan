package io.deccan.controlplane.scheduler.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.deccan.controlplane.scheduler.engine.WorkflowScheduler;
import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
import io.deccan.controlplane.scheduler.enums.ScheduleType;
import io.deccan.controlplane.scheduler.repository.WorkflowScheduleRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowScheduleServiceImpl
        implements WorkflowScheduleService {

    private final WorkflowRepository workflowRepository;

    private final WorkflowScheduleRepository scheduleRepository;
    private final WorkflowScheduler workflowScheduler;


    @Override
    public WorkflowSchedule createSchedule(
            UUID workflowId,
            WorkflowSchedule schedule) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        schedule.setWorkflow(workflow);

        schedule =
                scheduleRepository.save(schedule);

        if (schedule.getType() == ScheduleType.CRON &&
                Boolean.TRUE.equals(schedule.getEnabled())) {

            workflowScheduler.register(
                    schedule);

        }

        return schedule;

    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowSchedule> getSchedules(
            UUID workflowId) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        return scheduleRepository.findByWorkflow(
                workflow);

    }
    @Override
    public void enableSchedule(
            UUID scheduleId) {

        WorkflowSchedule schedule =
                scheduleRepository.findById(scheduleId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Schedule not found"));

        schedule.setEnabled(true);

        scheduleRepository.save(schedule);

        if (schedule.getType() == ScheduleType.CRON) {

            workflowScheduler.register(
                    schedule);

        }

    }
    @Override
    public void disableSchedule(
            UUID scheduleId) {

        WorkflowSchedule schedule =
                scheduleRepository.findById(scheduleId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Schedule not found"));

        schedule.setEnabled(false);

        scheduleRepository.save(schedule);

        workflowScheduler.unregister(
                schedule.getId());

}

}