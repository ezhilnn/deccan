package io.deccan.worker.logging;

import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.deccan.worker.registration.WorkerState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WorkerMdcFilter {

    private final WorkerState workerState;

    private final LoggingContextHolder
            contextHolder;

    public void initialize(
            ExecutionTaskResponse task){

        contextHolder.set(
                LoggingContext.builder()
                        .workerId(
                                workerState.getWorkerId().toString())
                        .executionId(
                                task.getExecutionId().toString())
                        .taskId(
                                task.getId().toString())
                        .workflowVersion(task.getWorkflowVersion())
                        .correlationId(
                                UUID.randomUUID().toString())
                        .build());

    }

    public void clear(){

        contextHolder.clear();

    }

}