package io.deccan.controlplane.worker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.deccan.controlplane.worker.entity.Worker;
import io.deccan.controlplane.worker.enums.WorkerStatus;

public interface WorkerRepository
        extends JpaRepository<Worker, UUID> {

    Optional<Worker> findByWorkerName(
            String workerName);

    List<Worker> findByStatus(
            WorkerStatus status);
    List<Worker> findByStatusOrderByLastHeartbeatDesc(
        WorkerStatus status);
    

}