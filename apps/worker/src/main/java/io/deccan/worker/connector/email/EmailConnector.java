package io.deccan.worker.connector.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.worker.connector.Connector;
import io.deccan.worker.connector.ConnectorResult;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConnector
        implements Connector{

    private final ObjectMapper objectMapper;

    @Override
    public String type(){

        return "email";

    }

    @Override
    public ConnectorResult execute(
            ExecutionTaskResponse task){

        return ConnectorResult.builder()
                .success(true)
                .output(
                        objectMapper.createObjectNode())
                .build();

    }

}