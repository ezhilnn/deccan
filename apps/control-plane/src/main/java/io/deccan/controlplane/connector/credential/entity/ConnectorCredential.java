package io.deccan.controlplane.connector.credential.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import io.deccan.controlplane.connector.credential.enums.CredentialType;
import io.deccan.controlplane.identity.entity.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="connector_credentials")
public class ConnectorCredential
        extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="organization_id")
    private Organization organization;

    @Column(nullable=false,length=100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=50)
    private CredentialType type;

    @Column(nullable=false,length=100)
    private String provider;

    /**
     * Example:
     *
     * vault://openai
     *
     * aws-sm://prod/openai
     *
     * local://credential/openai
     */
    @Column(
            name="secret_reference",
            nullable=false,
            length=200)
    private String secretReference;

    @Column(nullable=false)
    private Boolean enabled=true;

}