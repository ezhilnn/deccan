package io.deccan.controlplane.workflow.definition.validation;

public class WorkflowValidationException
        extends RuntimeException {

    public WorkflowValidationException(String message) {
        super(message);
    }

}