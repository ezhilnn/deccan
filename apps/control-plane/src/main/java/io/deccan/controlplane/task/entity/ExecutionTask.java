package io.deccan.controlplane.task.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "execution_tasks")
public class ExecutionTask extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "execution_id",
            nullable = false)
    private WorkflowExecution execution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Column(nullable = false)
    private String nodeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    private Instant leaseUntil;

}