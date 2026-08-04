package io.deccan.worker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerShutdownRequest {

    private boolean graceful = true;

}