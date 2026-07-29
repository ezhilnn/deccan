package io.deccan.worker.service;

import java.util.UUID;

public interface TaskResultService {

    void reportSuccess(
            UUID taskId);

    void reportFailure(
            UUID taskId);

}