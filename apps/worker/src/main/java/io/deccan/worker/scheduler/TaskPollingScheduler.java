package io.deccan.worker.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.deccan.worker.service.TaskPollingService;
import lombok.RequiredArgsConstructor;

@Component

@RequiredArgsConstructor
public class TaskPollingScheduler {

    private final TaskPollingService
            pollingService;

    @Scheduled(fixedDelayString = "${worker.polling.interval}")
    public void poll() {

        pollingService.poll();

    }

}