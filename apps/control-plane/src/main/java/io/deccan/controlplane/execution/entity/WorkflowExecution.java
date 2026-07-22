package io.deccan.controlplane.execution.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.workflow.entity.Workflow;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name="workflow_executions")
public class WorkflowExecution extends BaseEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="workflow_id")
    private Workflow workflow;

    @Column(name="workflow_version",nullable=false)
    private Integer workflowVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=50)
    private ExecutionStatus status;

    @Column(name="started_at",nullable=false)
    private OffsetDateTime startedAt;

    @Column(name="finished_at")
    private OffsetDateTime finishedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition="jsonb")
    private JsonNode input;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition="jsonb")
    private JsonNode output;

    @Column(name="error_message")
    private String errorMessage;

}