package io.deccan.controlplane.workflow.lifecycle;

public class WorkflowLifecycleException
        extends RuntimeException {

    public WorkflowLifecycleException(
            String message) {

        super(message);

    }

}