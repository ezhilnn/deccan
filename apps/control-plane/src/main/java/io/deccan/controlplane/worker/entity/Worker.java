package io.deccan.controlplane.worker.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.worker.enums.WorkerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workers")
public class Worker extends BaseEntity {

    @Column(name = "worker_name", nullable = false, unique = true)
    private String workerName;

    @Column(name = "host_name", nullable = false)
    private String hostName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkerStatus status;

    @Column(name = "last_heartbeat", nullable = false)
    private Instant lastHeartbeat;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private JsonNode capabilities;

}