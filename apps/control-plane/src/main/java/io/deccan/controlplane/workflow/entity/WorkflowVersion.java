package io.deccan.controlplane.workflow.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "workflow_versions",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "workflow_id",
                                "version"
                        }
                )
        }
)
public class WorkflowVersion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @Column(nullable = false)
    private Integer version;

    @Column(
            nullable = false,
            columnDefinition = "jsonb"
    )
    private String definition;

    @Column(nullable = false)
    private Boolean published;

}