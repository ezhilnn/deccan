package io.deccan.worker.logging;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class LoggingContextHolder {

    public void set(
            LoggingContext context){

        MDC.put(
                "workerId",
                context.getWorkerId());

        MDC.put(
                "executionId",
                context.getExecutionId());

        MDC.put(
                "taskId",
                context.getTaskId());

        MDC.put(
                "workflowId",
                context.getWorkflowId());

        MDC.put(
                "correlationId",
                context.getCorrelationId());

    }

    public void clear(){

        MDC.clear();

    }

}