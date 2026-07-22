package io.deccan.controlplane.connector.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.connector.enums.ConnectorType;
import io.deccan.controlplane.identity.entity.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "connectors")
public class Connector extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "display_name", nullable = false, length = 200)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ConnectorType type;

    @Column(nullable = false, length = 30)
    private String version;

    @Column(nullable = false)
    private Boolean enabled = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            name = "configuration_schema",
            nullable = false,
            columnDefinition = "jsonb"
    )
    private JsonNode configurationSchema;

}