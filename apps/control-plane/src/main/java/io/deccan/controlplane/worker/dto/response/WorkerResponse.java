package io.deccan.controlplane.worker.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.worker.enums.WorkerStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class WorkerResponse {

    private UUID id;

    private String workerName;

    private String hostName;

    private WorkerStatus status;

    private Instant lastHeartbeat;

    private JsonNode capabilities;

    private Instant createdAt;

    private Instant updatedAt;

}