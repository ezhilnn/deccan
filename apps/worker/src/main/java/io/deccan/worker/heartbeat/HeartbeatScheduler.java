package io.deccan.worker.heartbeat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeartbeatScheduler {

    private final WorkerHeartbeatService
            heartbeatService;

    @Scheduled(fixedDelayString = "${worker.heartbeat.interval}")
    public void sendHeartbeat() {

        heartbeatService.heartbeat();

    }

}