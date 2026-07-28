package io.deccan.controlplane.worker.service;

import io.deccan.controlplane.worker.entity.Worker;

import java.util.List;
import java.util.UUID;

public interface WorkerService {

    Worker register(
            Worker worker);

    Worker get(
            UUID workerId);

    List<Worker> list();
    Worker heartbeat(
        UUID workerId);

}