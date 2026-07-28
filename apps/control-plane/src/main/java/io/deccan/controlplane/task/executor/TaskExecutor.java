package io.deccan.controlplane.task.executor;

import io.deccan.controlplane.task.entity.ExecutionTask;

public interface TaskExecutor {

    void execute(
            ExecutionTask task);

}