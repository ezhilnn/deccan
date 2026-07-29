package io.deccan.worker.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class WorkerResponse {

    private UUID id;

    private String workerName;

    private String hostName;

    private String status;

    private Instant lastHeartbeat;

    private JsonNode capabilities;

    private Instant createdAt;

    private Instant updatedAt;

}