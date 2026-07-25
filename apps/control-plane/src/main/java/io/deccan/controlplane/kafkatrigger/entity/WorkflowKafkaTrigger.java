package io.deccan.controlplane.kafkatrigger.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.workflow.entity.Workflow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workflow_kafka_triggers")
public class WorkflowKafkaTrigger extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(nullable = false, unique = true)
    private String topic;

    @Column(nullable = false)
    private Boolean enabled = true;

}