package io.deccan.worker.metrics;

public interface WorkerMetricsService {

    void taskStarted();

    void taskSucceeded();

    void taskFailed();

    void taskTimedOut();

    void connectorExecuted(
            String connector);

    void executionFinished(
            long durationMillis);

}