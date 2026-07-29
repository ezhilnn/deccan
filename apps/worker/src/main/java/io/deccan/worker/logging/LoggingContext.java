package io.deccan.worker.logging;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoggingContext {

    private String workerId;

    private String executionId;

    private String taskId;

    private String workflowId;

    private String correlationId;

}