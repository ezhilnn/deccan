package io.deccan.worker.heartbeat;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import io.deccan.worker.registration.WorkerState;
import io.deccan.worker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerHeartbeatServiceImpl
        implements WorkerHeartbeatService {

    private final RestClient restClient;

    private final WorkerState workerState;

    private final AuthenticationService authenticationService;

    @Override
    public void heartbeat() {

        if (!workerState.isRegistered()) {
            return;
        }

        UUID workerId = workerState.getWorkerId();

        String token = authenticationService.getToken();

        restClient
                .post()
                .uri("/workers/{workerId}/heartbeat", workerId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        workerState.setLastHeartbeat(Instant.now());

        log.debug(
                "Heartbeat sent for worker {}",
                workerId);

    }

}