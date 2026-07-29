package io.deccan.worker.service;

import io.deccan.worker.connector.ConnectorExecutor;
import io.deccan.worker.connector.ConnectorResult;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskExecutionServiceImpl
        implements TaskExecutionService {

    private final ConnectorExecutor connectorExecutor;

    private final TaskResultService taskResultService;

    @Override
    public void execute(
            ExecutionTaskResponse task) {

        log.info("----------------------------------------");
        log.info("Executing Task");
        log.info("Task Id   : {}", task.getId());
        log.info("Node Type : {}", task.getNodeType());

        try {

            ConnectorResult result =
                    connectorExecutor.execute(task);

            if(result.isSuccess()){

                taskResultService.reportSuccess(
                        task.getId());

                log.info(
                        "Task completed successfully.");

            }
            else{

                taskResultService.reportFailure(
                        task.getId());

                log.error(
                        "Connector failed : {}",
                        result.getErrorMessage());

            }

        }
        catch (Exception ex){

            taskResultService.reportFailure(
                    task.getId());

            log.error(
                    "Task execution failed.",
                    ex);

        }

        log.info("----------------------------------------");

    }

}