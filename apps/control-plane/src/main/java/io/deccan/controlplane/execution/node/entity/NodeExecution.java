package io.deccan.controlplane.execution.node.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "node_executions")
public class NodeExecution extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_execution_id")
    private WorkflowExecution workflowExecution;

    @Column(nullable = false)
    private String nodeId;

    @Column(nullable = false)
    private String nodeType;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    private OffsetDateTime startedAt;

    private OffsetDateTime finishedAt;

    private Long durationMs;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode output;

    private String errorMessage;

}