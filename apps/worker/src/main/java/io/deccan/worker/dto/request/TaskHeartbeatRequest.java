package io.deccan.worker.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskHeartbeatRequest {

    private long extendBySeconds = 60;

}