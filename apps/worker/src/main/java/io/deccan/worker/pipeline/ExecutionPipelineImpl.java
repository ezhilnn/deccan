package io.deccan.worker.pipeline;

import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.deccan.worker.service.TaskExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExecutionPipelineImpl
        implements io.deccan.worker.pipeline.ExecutionPipeline {

    private final TaskExecutionService
            taskExecutionService;

    @Override
    public ExecutionResult execute(
            ExecutionTaskResponse task) {

        try {

            log.info(
                    "Starting execution pipeline for task {}",
                    task.getId());

            taskExecutionService.execute(task);

            return ExecutionResult.builder()
                    .success(true)
                    .message("Execution completed")
                    .build();

        }
        catch (Exception ex){

            log.error(
                    "Pipeline execution failed.",
                    ex);

            return ExecutionResult.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .build();

        }

    }

}