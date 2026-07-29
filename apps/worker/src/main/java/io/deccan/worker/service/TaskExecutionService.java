package io.deccan.worker.service;

import io.deccan.worker.dto.response.ExecutionTaskResponse;

public interface TaskExecutionService {

    void execute(
            ExecutionTaskResponse task);

}