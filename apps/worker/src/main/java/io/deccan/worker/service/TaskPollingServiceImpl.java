package io.deccan.worker.service;

import io.deccan.worker.dto.response.ApiResponse;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.deccan.worker.registration.WorkerState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskPollingServiceImpl
        implements TaskPollingService {

    private final RestClient restClient;

    private final WorkerState workerState;
    private final TaskExecutionService taskExecutionService;

    @Override
    public void poll() {

        if (!workerState.isRegistered()) {
            return;
        }

        try {

            ApiResponse<ExecutionTaskResponse> response =
                    restClient
                            .post()
                            .uri("/tasks/lease")
                            .retrieve()
                            .body(new ParameterizedTypeReference<ApiResponse<ExecutionTaskResponse>>() {});

            if (response == null || response.getData() == null) {
                return;
            }

            ExecutionTaskResponse task =
                    response.getData();

            taskExecutionService.execute(task);

        }
        catch (Exception ex) {
                
            log.error(
                    "Failed while polling for tasks.",
                    ex);
        }

    }

}