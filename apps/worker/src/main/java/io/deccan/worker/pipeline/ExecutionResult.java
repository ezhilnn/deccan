package io.deccan.worker.pipeline;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutionResult {

    private boolean success;

    private String message;

}