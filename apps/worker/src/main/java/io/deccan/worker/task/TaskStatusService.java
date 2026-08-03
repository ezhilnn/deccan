package io.deccan.worker.task;

import io.deccan.worker.dto.response.ExecutionTaskResponse;

import java.util.UUID;

public interface TaskStatusService {

    ExecutionTaskResponse getTask(
            UUID taskId);

}