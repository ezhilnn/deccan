package io.deccan.worker.service;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import io.deccan.worker.dto.request.TaskResultRequest;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class TaskResultServiceImpl
        implements TaskResultService {

    private final RestClient restClient;

    private final AuthenticationService authenticationService;

    @Override
    public void reportSuccess(
        UUID taskId,
        JsonNode output) {

        TaskResultRequest request = new TaskResultRequest();
        request.setSuccess(true);
        request.setOutput(
        output);

        String token = authenticationService.getToken();

        restClient
                .post()
                .uri("/tasks/{taskId}/result", taskId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();

    }

    @Override
    public void reportFailure(UUID taskId,String errorMessage) {

        TaskResultRequest request = new TaskResultRequest();
        request.setSuccess(false);
        request.setErrorMessage(errorMessage);

        String token = authenticationService.getToken();

        restClient
                .post()
                .uri("/tasks/{taskId}/result", taskId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .retrieve()
                .toBodilessEntity();

    }

}