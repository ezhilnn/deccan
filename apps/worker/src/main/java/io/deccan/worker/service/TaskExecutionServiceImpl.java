package io.deccan.worker.service;

import io.deccan.worker.connector.ConnectorExecutor;
import io.deccan.worker.connector.ConnectorResult;
import io.deccan.worker.context.ExecutionContextHolder;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.deccan.worker.retry.RetryExecutor;
import io.deccan.worker.retry.RetryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.worker.retry.RetryPolicy;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskExecutionServiceImpl
        implements TaskExecutionService {

    private final ConnectorExecutor connectorExecutor;

    private final TaskResultService taskResultService;
    private final ExecutionContextHolder
        contextHolder;

    private final ObjectMapper
            objectMapper;
    private final RetryExecutor
        retryExecutor;

    @Override
    public void execute(
            ExecutionTaskResponse task) {
        
        contextHolder.clear();

        contextHolder
                .get()
                .put(
                        "task",
                        objectMapper.valueToTree(task));

        log.info("----------------------------------------");
        log.info("Executing Task");
        log.info("Task Id   : {}", task.getId());
        log.info("Node Type : {}", task.getNodeType());

        try {

            RetryPolicy retryPolicy =
            RetryPolicy.builder()
                    .maxAttempts(3)
                    .initialDelay(1000)
                    .multiplier(2.0)
                    .maxDelay(10000)
                    .build();

            RetryResult retryResult =
                    retryExecutor.execute(
                            retryPolicy,
                            () -> {

                                ConnectorResult result =
                                        connectorExecutor.execute(task);

                                if(!result.isSuccess()){

                                    throw new RuntimeException(
                                            result.getErrorMessage());

                                }

                            });

            if(retryResult.isSuccess()){

                taskResultService.reportSuccess(
                        task.getId());

            }
            else{

                taskResultService.reportFailure(
                        task.getId());

            }

        }
        catch (Exception ex){

            taskResultService.reportFailure(
                    task.getId());

            log.error(
                    "Task execution failed.",
                    ex);

        }
        finally{
            contextHolder.clear();
        }

        log.info("----------------------------------------");

    }

}