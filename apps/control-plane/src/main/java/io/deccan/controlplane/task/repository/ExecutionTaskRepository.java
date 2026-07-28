package io.deccan.controlplane.task.repository;

import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ExecutionTaskRepository
        extends JpaRepository<ExecutionTask, UUID> {

    List<ExecutionTask> findByStatus(
            TaskStatus status);

    List<ExecutionTask> findByStatusAndLeaseUntilBefore(

            TaskStatus status,

            Instant now

    );

}