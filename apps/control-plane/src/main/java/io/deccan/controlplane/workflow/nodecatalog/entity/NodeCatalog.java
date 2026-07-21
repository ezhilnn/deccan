package io.deccan.controlplane.workflow.nodecatalog.entity;

import io.deccan.controlplane.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="node_catalog")
public class NodeCatalog extends BaseEntity {

    @Column(nullable=false,unique=true,length=100)
    private String name;

    @Column(name="display_name",nullable=false,length=200)
    private String displayName;

    @Column(nullable=false,length=100)
    private String category;

    @Column(nullable=false,length=200)
    private String implementation;

    @Column(nullable=false)
    private Boolean enabled;

}