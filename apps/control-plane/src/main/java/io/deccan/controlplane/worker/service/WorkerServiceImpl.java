package io.deccan.controlplane.worker.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.task.repository.ExecutionTaskRepository;
import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.enums.WorkerStatus;
import io.deccan.controlplane.worker.exception.WorkerNotFoundException;
import io.deccan.controlplane.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerServiceImpl
        implements WorkerService {

    private final WorkerRepository repository;
    private final ExecutionTaskRepository executionTaskRepository;
//     private final ExecutionTaskService executionTaskService;

    @Override
    public Worker register(
            Worker worker) {

        Worker existing =
                repository.findByWorkerName(worker.getWorkerName())
                        .orElse(null);

        if (existing != null) {

            existing.setHostName(worker.getHostName());
            existing.setCapabilities(worker.getCapabilities());
            existing.setStatus(WorkerStatus.ONLINE);
            existing.setLastHeartbeat(Instant.now());

            return repository.save(existing);
        }

        worker.setStatus(WorkerStatus.ONLINE);
        worker.setLastHeartbeat(Instant.now());

        return repository.save(worker);
    }

    @Override
    @Transactional(readOnly = true)
    public Worker get(
            UUID workerId) {

        return repository.findById(workerId)
                .orElseThrow(() ->
                       new WorkerNotFoundException(
        "Worker not found"));

    }
    

    @Override
    @Transactional(readOnly = true)
    public List<Worker> list() {

        return repository.findAll();

    }

    @Override
    public Worker heartbeat(
            UUID workerId) {

        Worker worker =
                repository.findById(workerId)
                        .orElseThrow(() ->
                                new WorkerNotFoundException(
        "Worker not found"));

        worker.setLastHeartbeat(
                Instant.now());

        worker.setStatus(
                WorkerStatus.ONLINE);

        return repository.save(
                worker);

    }

    @Override
    @Transactional(readOnly = true)
    public Worker findAvailableWorker() {

        return repository
                .findByStatusOrderByLastHeartbeatDesc(
                        WorkerStatus.ONLINE)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No online worker available"));

    }
    @Override
        public Worker shutdown(
                UUID workerId) {

        Worker worker =
                repository.findById(workerId)
                        .orElseThrow(() ->
                                new WorkerNotFoundException(
        "Worker not found"));

        worker.setStatus(
                WorkerStatus.OFFLINE);

        worker.setLastHeartbeat(
                Instant.now());

        return repository.save(worker);

        }
        @Override
        public int markOfflineWorkers() {

        Instant cutoff =
                Instant.now()
                        .minusSeconds(90);

        List<Worker> workers =
                repository.findByStatus(
                        WorkerStatus.ONLINE);

        int updated = 0;

        for (Worker worker : workers) {

                if (worker.getLastHeartbeat() == null) {
                continue;
                }

                if (worker.getLastHeartbeat().isBefore(cutoff)) {

                worker.setStatus(
                        WorkerStatus.OFFLINE);

                repository.save(worker);

                updated++;

                }

        }

        return updated;

        }
 @Override
public int recoverExpiredLeases() {

       List<ExecutionTask> tasks =
            executionTaskRepository
                    .findByStatusAndLeaseUntilBefore(
                            TaskStatus.LEASED,
                            Instant.now());

    int recovered = 0;

    for (ExecutionTask task : tasks) {

        task.setStatus(TaskStatus.READY);

        task.setWorker(null);

        task.setLeaseUntil(null);

        task.setLeasedAt(null);

        executionTaskRepository.save(task);

        recovered++;

    }

    return recovered;

}

}