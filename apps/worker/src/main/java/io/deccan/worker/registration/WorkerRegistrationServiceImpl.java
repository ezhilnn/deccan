package io.deccan.worker.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.worker.config.WorkerProperties;
import io.deccan.worker.dto.request.RegisterWorkerRequest;
import io.deccan.worker.dto.response.ApiResponse;
import io.deccan.worker.dto.response.WorkerResponse;
import io.deccan.worker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WorkerRegistrationServiceImpl
        implements WorkerRegistrationService {

    private final RestClient restClient;

    private final WorkerProperties workerProperties;

    private final WorkerState workerState;

    private final ObjectMapper objectMapper;

    private final AuthenticationService authenticationService;

    @Override
    public WorkerResponse register() {

        RegisterWorkerRequest request = new RegisterWorkerRequest();

        request.setWorkerName(workerProperties.getName());

        request.setHostName(workerProperties.getHost());

        request.setCapabilities(
                objectMapper.valueToTree(
                        workerProperties.getCapabilities()));

        String token = authenticationService.getToken();

        ApiResponse<WorkerResponse> response =
                restClient
                        .post()
                        .uri("/workers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body(request)
                        .retrieve()
                        .body(new ParameterizedTypeReference<ApiResponse<WorkerResponse>>() {});

        if (response == null || response.getData() == null) {
            throw new IllegalStateException("Failed to register worker.");
        }

        WorkerResponse worker = response.getData();

        workerState.setRegistered(true);
        workerState.setWorkerId(worker.getId());
        workerState.setWorkerName(worker.getWorkerName());
        workerState.setLastHeartbeat(Instant.now());

        return worker;
    }
}