package io.deccan.controlplane.execution.connector.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.connector.ConnectorRequest;
import io.deccan.controlplane.execution.connector.ConnectorResponse;
import io.deccan.controlplane.execution.connector.ConnectorRuntime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpConnectorRuntime implements ConnectorRuntime {

    private final RestClient restClient;
    

    @Override
    public boolean supports(String connector) {
        return "http".equalsIgnoreCase(connector);
    }

    @Override
    public ConnectorResponse execute(ConnectorRequest request) {

        JsonNode configuration = request.getConfiguration();

        String method = configuration
                .path("method")
                .asText("GET")
                .toUpperCase();

        String url = configuration
                .path("url")
                .asText();

        if (url.isBlank()) {
            throw new IllegalArgumentException("URL is required");
        }

        HttpMethod httpMethod;

        try {
            httpMethod = HttpMethod.valueOf(method);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Unsupported HTTP method: " + method);
        }

        try {

            Object response = restClient
                    .method(httpMethod)
                    .uri(url)
                    .retrieve()
                    .body(Object.class);

            Map<String, Object> result = new HashMap<>();

            result.put("status", 200);
            result.put("body", response);

            log.info(
                    "HTTP connector executed {} {}",
                    method,
                    url);

            return ConnectorResponse.builder()
                    .success(true)
                    .body(result)
                    .build();

        }
        catch (RestClientResponseException ex) {

            Map<String, Object> result = new HashMap<>();

            result.put("status", ex.getStatusCode().value());
            result.put("body", ex.getResponseBodyAsString());

            return ConnectorResponse.builder()
                    .success(false)
                    .body(result)
                    .error(ex.getMessage())
                    .build();

        }
        catch (Exception ex) {

            return ConnectorResponse.builder()
                    .success(false)
                    .error(ex.getMessage())
                    .build();

        }
    }
}