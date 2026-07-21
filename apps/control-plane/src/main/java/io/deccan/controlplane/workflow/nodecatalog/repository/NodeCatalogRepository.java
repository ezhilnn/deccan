package io.deccan.controlplane.workflow.nodecatalog.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.deccan.controlplane.workflow.nodecatalog.entity.NodeCatalog;

public interface NodeCatalogRepository
        extends JpaRepository<NodeCatalog,UUID>{

    Optional<NodeCatalog> findByName(String name);

    boolean existsByName(String name);

}