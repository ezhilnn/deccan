package io.deccan.worker.connector;

import io.deccan.worker.dto.response.ExecutionTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConnectorExecutor {

    private final ConnectorRegistry registry;

    public ConnectorResult execute(
            ExecutionTaskResponse task){

        return registry
                .get(task.getNodeType())
                .execute(task);

    }

}