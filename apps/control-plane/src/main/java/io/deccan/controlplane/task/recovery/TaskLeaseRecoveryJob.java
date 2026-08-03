package io.deccan.controlplane.task.recovery;

import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.service.ExecutionTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskLeaseRecoveryJob {

    private final ExecutionTaskService service;

    @Scheduled(fixedDelay = 30000)
    public void recover() {

        List<ExecutionTask> recovered =
                service.recoverExpiredLeases();

        if (!recovered.isEmpty()) {

            log.info(
                    "Recovered {} expired task lease(s).",
                    recovered.size());

        }

    }

}