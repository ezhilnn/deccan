package io.deccan.controlplane.task.dispatcher;

import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.executor.TaskExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskDispatcher {

    private final TaskExecutor executor;

    public void dispatch(
            ExecutionTask task){

        executor.execute(
                task);

    }

}