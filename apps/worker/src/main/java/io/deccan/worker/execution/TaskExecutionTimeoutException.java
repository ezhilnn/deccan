package io.deccan.worker.execution;

public class TaskExecutionTimeoutException
        extends RuntimeException {

    public TaskExecutionTimeoutException(
            String message) {

        super(message);

    }

}