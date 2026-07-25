package io.deccan.controlplane.scheduler.engine;

import io.deccan.controlplane.execution.service.ExecutionService;
import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
public class WorkflowScheduler {

    private final TaskScheduler taskScheduler;

    private final ExecutionService executionService;

    private final Map<UUID, ScheduledFuture<?>> schedules =
            new ConcurrentHashMap<>();

    public void register(
            WorkflowSchedule schedule) {
        
        if (schedules.containsKey(
                schedule.getId())) {

            unregister(
                    schedule.getId());

        }

        ScheduledFuture<?> future =
                taskScheduler.schedule(

                        () -> executionService.executeWorkflow(
                                schedule.getWorkflow().getId(),
                                null),

                        new CronTrigger(
                                schedule.getCronExpression())

                );

        schedules.put(
                schedule.getId(),
                future);

    }

    public void unregister(
            UUID scheduleId) {

        ScheduledFuture<?> future =
                schedules.remove(scheduleId);

        if (future != null) {

            future.cancel(false);

        }

    }

}