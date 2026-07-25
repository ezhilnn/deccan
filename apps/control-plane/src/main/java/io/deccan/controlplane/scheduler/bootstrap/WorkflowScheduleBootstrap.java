package io.deccan.controlplane.scheduler.bootstrap;

import io.deccan.controlplane.scheduler.engine.WorkflowScheduler;
import io.deccan.controlplane.scheduler.entity.WorkflowSchedule;
import io.deccan.controlplane.scheduler.enums.ScheduleType;
import io.deccan.controlplane.scheduler.repository.WorkflowScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowScheduleBootstrap
        implements CommandLineRunner {

    private final WorkflowScheduleRepository repository;

    private final WorkflowScheduler scheduler;

    @Override
    public void run(String... args) {

        repository.findByEnabledTrue()

                .stream()

                .filter(schedule ->
                        schedule.getType() ==
                                ScheduleType.CRON)

                .forEach(schedule -> {

                    scheduler.register(schedule);

                    log.info(
                            "Registered schedule [{}]",
                            schedule.getId());

                });

    }

}