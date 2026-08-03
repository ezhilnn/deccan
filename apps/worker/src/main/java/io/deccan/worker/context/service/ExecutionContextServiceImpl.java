package io.deccan.worker.context.service;

import io.deccan.worker.context.dto.response.ExecutionContextResponse;
import io.deccan.worker.dto.response.ApiResponse;
import io.deccan.worker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExecutionContextServiceImpl
        implements ExecutionContextService {

    private final RestClient restClient;

    private final AuthenticationService authenticationService;

    @Override
    public ExecutionContextResponse getContext(
            UUID executionId) {

        String token =
                authenticationService.getToken();

        ApiResponse<ExecutionContextResponse> response =
                restClient
                        .get()
                        .uri("/executions/{executionId}/context", executionId)
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token)
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<ApiResponse<ExecutionContextResponse>>() {
                                });

        return response.getData();

    }

}