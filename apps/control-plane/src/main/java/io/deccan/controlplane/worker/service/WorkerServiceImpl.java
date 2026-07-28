package io.deccan.controlplane.worker.service;

import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.enums.WorkerStatus;
import io.deccan.controlplane.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerServiceImpl
        implements WorkerService {

    private final WorkerRepository repository;

    @Override
    public Worker register(
            Worker worker){

        worker.setStatus(
                WorkerStatus.ONLINE);

        worker.setLastHeartbeat(
                Instant.now());

        return repository.save(
                worker);

    }

    @Override
    @Transactional(readOnly = true)
    public Worker get(
            UUID workerId){

        return repository.findById(workerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Worker not found"));

    }

    @Override
    @Transactional(readOnly = true)
    public List<Worker> list(){

        return repository.findAll();

    }
    @Override
    public Worker heartbeat(
            UUID workerId){

        Worker worker =
                repository.findById(workerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
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
    public Worker findAvailableWorker(){

        return repository
                .findByStatusOrderByLastHeartbeatDesc(
                        WorkerStatus.ONLINE)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No online worker available"));

    }

}