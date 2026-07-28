package io.deccan.controlplane.task.service;

import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.task.repository.ExecutionTaskRepository;
import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExecutionTaskServiceImpl
        implements ExecutionTaskService {

    private final ExecutionTaskRepository taskRepository;

    private final WorkerRepository workerRepository;

    @Override
    public ExecutionTask leaseTask(
            UUID workerId) {

        Worker worker =
                workerRepository.findById(workerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Worker not found"));

        Instant now =
                Instant.now();

        ExecutionTask task =
        taskRepository
                .leaseNextTask(

                        TaskStatus.PENDING,

                        org.springframework.data.domain.PageRequest.of(
                                0,
                                1)

                )
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No pending task available"));

        task.setWorker(
                worker);

        task.setStatus(
                TaskStatus.LEASED);

        task.setLeaseUntil(
                now.plusSeconds(60));

        return taskRepository.save(
                task);

    }

}