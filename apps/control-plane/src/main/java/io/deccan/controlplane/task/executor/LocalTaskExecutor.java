package io.deccan.controlplane.task.executor;

import io.deccan.controlplane.task.entity.ExecutionTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocalTaskExecutor implements TaskExecutor {

    @Override
    public void execute(
            ExecutionTask task) {

        log.info(
                "Executing task [{}] for execution [{}] (dummy executor)",
                task.getId(),
                task.getExecution().getId());

        // TODO: Replace with actual worker implementation.
    }

}