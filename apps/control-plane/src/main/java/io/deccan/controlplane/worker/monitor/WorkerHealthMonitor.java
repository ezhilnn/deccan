package io.deccan.controlplane.worker.monitor;

import io.deccan.controlplane.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkerHealthMonitor {

    private final WorkerService workerService;

    @Scheduled(fixedDelay = 30000)
    public void monitor() {

        int offlineWorkers =
                workerService.markOfflineWorkers();

        if (offlineWorkers > 0) {

            log.info(
                    "Marked {} worker(s) OFFLINE.",
                    offlineWorkers);

        }

    }

}