package io.deccan.controlplane.workflow.exception;

public class WorkflowNotFoundException
        extends RuntimeException {

    public WorkflowNotFoundException(String message) {
        super(message);
    }

}