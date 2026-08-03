package io.deccan.controlplane.task.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "execution_tasks")
public class ExecutionTask extends BaseEntity {


    @Column(name = "execution_id", nullable = false, insertable = false, updatable = false)

    private UUID executionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "execution_id",
            nullable = false)
    private WorkflowExecution execution;

    @Column(name = "worker_id", insertable = false, updatable = false)

    private UUID workerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Column(nullable = false)
    private String nodeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    private Instant leaseUntil;

    @Column(name = "leased_at")
    private Instant leasedAt;
    
    @Column(nullable = false)
    private String nodeType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private JsonNode configuration;

    private String errorMessage;

    private Instant startedAt;

    private Instant completedAt;

    @Column(nullable = false)
    private Integer retryCount = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode output;

}