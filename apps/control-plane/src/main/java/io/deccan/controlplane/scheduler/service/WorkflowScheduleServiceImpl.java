package io.deccan.controlplane.scheduler.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
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

        return scheduleRepository.save(schedule);

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

}