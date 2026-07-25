package io.deccan.controlplane.kafkatrigger.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.kafkatrigger.entity.WorkflowKafkaTrigger;

import java.util.UUID;

public interface WorkflowKafkaTriggerService {

    WorkflowKafkaTrigger registerTrigger(
            UUID workflowId,
            String topic);

    WorkflowExecution executeTrigger(
            String topic,
            JsonNode payload);
    void disableTrigger(
        UUID triggerId);

    void deleteTrigger(
            UUID triggerId);

}