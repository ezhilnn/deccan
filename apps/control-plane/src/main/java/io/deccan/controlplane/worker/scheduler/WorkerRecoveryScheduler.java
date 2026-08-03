package io.deccan.controlplane.worker.scheduler;

import io.deccan.controlplane.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkerRecoveryScheduler {

    private final WorkerService workerService;

    @Scheduled(fixedDelay = 30000)
    public void recoverWorkers() {

        int offline =
                workerService.markOfflineWorkers();

        int recovered =
                workerService.recoverExpiredLeases();

        if (offline > 0 || recovered > 0) {

            log.info(
                    "Recovered {} expired tasks. Marked {} workers offline.",
                    recovered,
                    offline);

        }

    }

}