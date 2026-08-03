package io.deccan.controlplane.task.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskHeartbeatRequest {

    private long extendBySeconds = 60;

}