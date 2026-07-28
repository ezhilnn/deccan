package io.deccan.controlplane.worker.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class HeartbeatResponse {

    private Instant lastHeartbeat;

    private String message;

}