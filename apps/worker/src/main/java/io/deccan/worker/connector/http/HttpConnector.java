package io.deccan.worker.connector.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.worker.connector.Connector;
import io.deccan.worker.connector.ConnectorResult;
import io.deccan.worker.context.ConfigurationResolver;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpConnector
        implements Connector {

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    private final ConfigurationResolver
        configurationResolver;

    @Override
    public String type() {

        return "http";

    }

    @Override
    public ConnectorResult execute(
            ExecutionTaskResponse task) {

        try {

            JsonNode resolvedConfiguration =
                configurationResolver.resolve(
                    task.getConfiguration().deepCopy());

            HttpRequestConfig config =
                    objectMapper.treeToValue(
                            resolvedConfiguration,
                            HttpRequestConfig.class);

            ResponseEntity<String> response =
                    executeRequest(config);

            JsonNode responseBody;

            try {

                responseBody =
                        objectMapper.readTree(
                                response.getBody());

            } catch (Exception ex) {

                responseBody =
                        objectMapper.createObjectNode()
                                .put(
                                        "body",
                                        response.getBody());

            }

            HttpResponseData data =
                    HttpResponseData.builder()
                            .status(
                                    response.getStatusCode().value())
                            .body(
                                    responseBody)
                            .build();

            return ConnectorResult.builder()
                    .success(true)
                    .output(
                            objectMapper.valueToTree(data))
                    .build();

        }
        catch (Exception ex){

            log.error(
                    "HTTP connector execution failed.",
                    ex);

            return ConnectorResult.builder()
                    .success(false)
                    .errorMessage(ex.getMessage())
                    .build();

        }

    }

    private ResponseEntity<String> executeRequest(
            HttpRequestConfig config){

        RestClient.RequestBodySpec request =
                restClient.method(
                        HttpMethod.valueOf(
                                config.getMethod().toUpperCase()))
                        .uri(config.getUrl());

        if(config.getHeaders()!=null){

            config.getHeaders().forEach(
                    request::header);

        }

        if(config.getBody()!=null){

            request.body(config.getBody());

        }

        return request.retrieve()
                .toEntity(String.class);

    }

}