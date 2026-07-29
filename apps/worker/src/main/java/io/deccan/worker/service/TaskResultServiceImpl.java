package io.deccan.worker.service;

import io.deccan.worker.dto.request.TaskResultRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskResultServiceImpl
        implements TaskResultService {

    private final RestClient restClient;

    @Override
    public void reportSuccess(
            UUID taskId) {

        TaskResultRequest request =
                new TaskResultRequest();

        request.setSuccess(true);

        restClient
                .post()
                .uri("/tasks/{taskId}/result", taskId)
                .body(request)
                .retrieve()
                .toBodilessEntity();

    }

    @Override
    public void reportFailure(
            UUID taskId) {

        TaskResultRequest request =
                new TaskResultRequest();

        request.setSuccess(false);

        restClient
                .post()
                .uri("/tasks/{taskId}/result", taskId)
                .body(request)
                .retrieve()
                .toBodilessEntity();

    }

}