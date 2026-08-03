package io.deccan.worker.context.service;

import io.deccan.worker.context.dto.response.ExecutionContextResponse;

import java.util.UUID;

public interface ExecutionContextService {

    ExecutionContextResponse getContext(
            UUID executionId);

}