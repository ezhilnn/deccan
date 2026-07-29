package io.deccan.worker.pipeline;

import io.deccan.worker.dto.response.ExecutionTaskResponse;

public interface ExecutionPipeline {

    ExecutionResult execute(
            ExecutionTaskResponse task);

}