package io.deccan.worker.service;

import io.deccan.worker.dto.response.ApiResponse;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.deccan.worker.pipeline.ExecutionPipeline;
import io.deccan.worker.registration.WorkerState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskPollingServiceImpl
        implements TaskPollingService {

    private final RestClient restClient;

    private final WorkerState workerState;

    private final ExecutionPipeline executionPipeline;

    private final AuthenticationService authenticationService;

    @Override
    public void poll() {

        if (!workerState.isRegistered()) {
            return;
        }

        try {

            String token = authenticationService.getToken();

            ResponseEntity<ApiResponse<ExecutionTaskResponse>> response =
                    restClient
                            .post()
                            .uri("/tasks/lease")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .retrieve()
                            .toEntity(new ParameterizedTypeReference<ApiResponse<ExecutionTaskResponse>>() {});

            if (response.getStatusCode().value() == 204) {
                return;
            }

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Unexpected response while polling tasks: {}", response.getStatusCode());
                return;
            }

            ApiResponse<ExecutionTaskResponse> body = response.getBody();

            if (body == null || body.getData() == null) {
                return;
            }

            executionPipeline.execute(body.getData());

        } catch (Exception ex) {

            log.error(
                    "Failed while polling for tasks.",
                    ex);

        }

    }

}