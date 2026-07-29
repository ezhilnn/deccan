package io.deccan.worker.execution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class TaskExecutor {

    private final ExecutionProperties
            properties;

    private final ExecutorService executor =
            Executors.newCachedThreadPool();

    public void execute(
            Runnable runnable) {

        Future<?> future =
                executor.submit(runnable);

        try {

            future.get(
                    properties.getTimeout(),
                    TimeUnit.MILLISECONDS);

        }
        catch (TimeoutException ex) {

            future.cancel(true);

            throw new TaskExecutionTimeoutException(
                    "Task execution timed out.");

        }
        catch (ExecutionException ex) {

            throw new RuntimeException(
                    ex.getCause());

        }
        catch (InterruptedException ex) {

            Thread.currentThread()
                    .interrupt();

            throw new RuntimeException(ex);

        }

    }

}