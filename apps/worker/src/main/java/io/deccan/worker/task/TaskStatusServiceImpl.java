package io.deccan.worker.task;

import io.deccan.worker.dto.response.ApiResponse;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.deccan.worker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskStatusServiceImpl
        implements TaskStatusService {

    private final RestClient restClient;

    private final AuthenticationService authenticationService;

    @Override
    public ExecutionTaskResponse getTask(
            UUID taskId) {

        String token =
                authenticationService.getToken();

        ApiResponse<ExecutionTaskResponse> response =
                restClient
                        .get()
                        .uri("/tasks/{taskId}", taskId)
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token)
                        .retrieve()
                        .body(new ParameterizedTypeReference<ApiResponse<ExecutionTaskResponse>>() {});

        return response.getData();

    }

}