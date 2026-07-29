package io.deccan.worker.registration;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class WorkerState {

    private UUID workerId;

    private String workerName;

    private boolean registered;

    private Instant startedAt = Instant.now();

    private Instant lastHeartbeat;

}