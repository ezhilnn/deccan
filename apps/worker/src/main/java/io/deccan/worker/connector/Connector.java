package io.deccan.worker.connector;

import io.deccan.worker.dto.response.ExecutionTaskResponse;

public interface Connector {

    String type();

    ConnectorResult execute(
            ExecutionTaskResponse task);

}