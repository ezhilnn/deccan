package io.deccan.worker.heartbeat;

import java.util.UUID;

public interface TaskHeartbeatService {

    void start(
            UUID taskId);

    void stop();

}