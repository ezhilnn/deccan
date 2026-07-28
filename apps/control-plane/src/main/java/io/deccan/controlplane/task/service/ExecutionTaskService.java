package io.deccan.controlplane.task.service;

import io.deccan.controlplane.task.entity.ExecutionTask;

import java.util.UUID;

public interface ExecutionTaskService {

    ExecutionTask leaseTask(
            UUID workerId);

}