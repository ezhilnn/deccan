package io.deccan.worker.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WorkerMetricsServiceImpl
        implements WorkerMetricsService {

    private final MeterRegistry
            meterRegistry;

    @Override
    public void taskStarted() {

        Counter.builder(
                WorkerMetrics.TASK_EXECUTIONS)
                .register(meterRegistry)
                .increment();

    }

    @Override
    public void taskSucceeded() {

        Counter.builder(
                WorkerMetrics.TASK_SUCCESS)
                .register(meterRegistry)
                .increment();

    }

    @Override
    public void taskFailed() {

        Counter.builder(
                WorkerMetrics.TASK_FAILURE)
                .register(meterRegistry)
                .increment();

    }

    @Override
    public void taskTimedOut() {

        Counter.builder(
                WorkerMetrics.TASK_TIMEOUT)
                .register(meterRegistry)
                .increment();

    }

    @Override
    public void connectorExecuted(
            String connector) {

        Counter.builder(
                WorkerMetrics.CONNECTOR_EXECUTIONS)
                .tag("connector", connector)
                .register(meterRegistry)
                .increment();

    }

    @Override
    public void executionFinished(
            long durationMillis) {

        Timer.builder(
                WorkerMetrics.TASK_DURATION)
                .register(meterRegistry)
                .record(
                        durationMillis,
                        TimeUnit.MILLISECONDS);

    }

}