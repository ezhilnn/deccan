package io.deccan.worker.shutdown;

import io.deccan.worker.dto.request.WorkerShutdownRequest;
import io.deccan.worker.registration.WorkerState;
import io.deccan.worker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
public class WorkerShutdownServiceImpl
        implements WorkerShutdownService {

    private final RestClient restClient;

    private final AuthenticationService authenticationService;

    private final WorkerState workerState;

    @Override
    @PreDestroy
    public void shutdown() {

        if (!workerState.isRegistered()) {
            return;
        }

        try {

            String token =
                    authenticationService.getToken();

            WorkerShutdownRequest request =
                    new WorkerShutdownRequest();

            restClient.post()
                    .uri("/workers/{workerId}/shutdown",
                            workerState.getWorkerId())
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + token)
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception ignored) {

        }

    }

}