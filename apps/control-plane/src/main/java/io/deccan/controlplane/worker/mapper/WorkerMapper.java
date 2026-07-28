package io.deccan.controlplane.worker.mapper;

import io.deccan.controlplane.worker.dto.request.RegisterWorkerRequest;
import io.deccan.controlplane.worker.dto.response.WorkerResponse;
import io.deccan.controlplane.worker.entity.Worker;
import org.springframework.stereotype.Component;

@Component
public class WorkerMapper {

    public Worker toEntity(
            RegisterWorkerRequest request){

        Worker worker = new Worker();

        worker.setWorkerName(
                request.getWorkerName());

        worker.setHostName(
                request.getHostName());

        worker.setCapabilities(
                request.getCapabilities());

        return worker;

    }

    public WorkerResponse toResponse(
            Worker worker){

        return WorkerResponse.builder()
                .id(worker.getId())
                .workerName(worker.getWorkerName())
                .hostName(worker.getHostName())
                .status(worker.getStatus())
                .lastHeartbeat(worker.getLastHeartbeat())
                .capabilities(worker.getCapabilities())
                .createdAt(worker.getCreatedAt())
                .updatedAt(worker.getUpdatedAt())
                .build();

    }

}