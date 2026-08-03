package io.deccan.worker.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public interface TaskResultService {

    void reportSuccess(
            UUID taskId,
            JsonNode output);

    void reportFailure(
            UUID taskId,
            String errorMessage);

}