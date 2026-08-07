package io.deccan.controlplane.worker.exception;

public class WorkerNotFoundException
        extends RuntimeException {

    public WorkerNotFoundException(String message) {
        super(message);
    }

}