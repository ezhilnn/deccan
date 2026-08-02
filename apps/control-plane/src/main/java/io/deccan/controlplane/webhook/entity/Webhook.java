package io.deccan.controlplane.webhook.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.workflow.entity.Workflow;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "webhooks",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "endpoint")
        }
)
public class Webhook extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workflow_id",
            nullable = false)
    private Workflow workflow;

    @Column(
            nullable = false,
            length = 150)
    private String endpoint;

    @Column(
            nullable = false,
            length = 500)
    private String secret;

    @Column(nullable = false)
    private Boolean enabled = true;

}