package io.deccan.controlplane.workflow.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workflows")
public class Workflow extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkflowStatus status;

    @Column(name = "current_version", nullable = false)
    private Integer currentVersion;

}