package io.deccan.worker.metrics;

public final class WorkerMetrics {

    private WorkerMetrics() {
    }

    public static final String TASK_EXECUTIONS =
            "deccan.worker.task.executions";

    public static final String TASK_SUCCESS =
            "deccan.worker.task.success";

    public static final String TASK_FAILURE =
            "deccan.worker.task.failure";

    public static final String TASK_TIMEOUT =
            "deccan.worker.task.timeout";

    public static final String TASK_DURATION =
            "deccan.worker.task.duration";

    public static final String CONNECTOR_EXECUTIONS =
            "deccan.worker.connector.executions";

}