package io.deccan.controlplane.task.repository;

import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExecutionTaskRepository
        extends JpaRepository<ExecutionTask, UUID> {

    List<ExecutionTask> findByStatus(
            TaskStatus status);

    List<ExecutionTask> findByStatusAndLeaseUntilBefore(
            TaskStatus status,
            Instant now);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT t
            FROM ExecutionTask t
            WHERE t.status = :status
            ORDER BY t.createdAt
            """)
    List<ExecutionTask> leaseNextTask(

            @Param("status")
            TaskStatus status,

            org.springframework.data.domain.Pageable pageable

    );

}