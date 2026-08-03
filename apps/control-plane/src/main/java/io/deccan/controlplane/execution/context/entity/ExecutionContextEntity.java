package io.deccan.controlplane.execution.context.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(
        name = "execution_context",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "execution_id",
                                "node_id"
                        }
                )
        }
)
public class ExecutionContextEntity
        extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "execution_id",
            nullable = false)
    private WorkflowExecution execution;

    @Column(
            name = "node_id",
            nullable = false,
            length = 255)
    private String nodeId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            nullable = false,
            columnDefinition = "jsonb")
    private JsonNode output;

}