package io.deccan.controlplane.task.service;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface ExecutionTaskService {

    ExecutionTask leaseTask(
            UUID workerId);
    List<ExecutionTask> createTasks(

            WorkflowExecution execution,

            WorkflowVersion version);

    void completeTask(
            UUID taskId);

    void failTask(
            UUID taskId,
            String reason);

    void reportSuccess(
        UUID taskId,
        JsonNode output);

    void reportFailure(
            UUID taskId,
            String errorMessage);
    List<ExecutionTask> getTasks(
        UUID executionId);
    ExecutionTask leaseNextTask();
    void heartbeat(
        UUID taskId,
        long extendBySeconds);
    List<ExecutionTask> recoverExpiredLeases();
    void cancelTasks(
        UUID executionId);
}