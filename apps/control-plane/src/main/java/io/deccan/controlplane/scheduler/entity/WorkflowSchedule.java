package io.deccan.controlplane.scheduler.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.scheduler.enums.ScheduleType;
import io.deccan.controlplane.workflow.entity.Workflow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workflow_schedules")
public class WorkflowSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType type;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(nullable = false)
    private Boolean enabled = true;

}