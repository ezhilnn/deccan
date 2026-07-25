package io.deccan.controlplane.kafkatrigger.repository;

import io.deccan.controlplane.kafkatrigger.entity.WorkflowKafkaTrigger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface WorkflowKafkaTriggerRepository
        extends JpaRepository<WorkflowKafkaTrigger, UUID> {

    Optional<WorkflowKafkaTrigger> findByTopic(
            String topic);
    List<WorkflowKafkaTrigger> findByEnabledTrue();

}