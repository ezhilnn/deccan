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
import java.time.Duration;
import java.time.Instant;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskPollingServiceImpl
        implements TaskPollingService {

    private final RestClient restClient;

    private final WorkerState workerState;

    private final ExecutionPipeline executionPipeline;

    private final AuthenticationService authenticationService;
    private static final Duration STARTUP_GRACE_PERIOD =
            Duration.ofMinutes(1);

    private final Instant startupTime =
            Instant.now();

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

        } catch (ResourceAccessException ex) {

            if (Instant.now().isBefore(
                    startupTime.plus(STARTUP_GRACE_PERIOD))) {

                log.info(
                        "Waiting for Control Plane...");

                return;

            }

            log.error(
                    "Unable to reach Control Plane.",
                    ex);

        }
        catch (Exception ex) {

            log.error(
                    "Failed while polling for tasks.",
                    ex);

        }

    }

}