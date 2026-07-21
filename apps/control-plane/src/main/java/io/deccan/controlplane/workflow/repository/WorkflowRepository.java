package io.deccan.controlplane.workflow.repository;

import io.deccan.controlplane.identity.entity.Organization;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {

        List<Workflow> findByOrganization(
                Organization organization
        );

        List<Workflow> findByOrganizationAndStatus(
                Organization organization,
                WorkflowStatus status
        );

        Optional<Workflow> findByOrganizationAndId(
                Organization organization,
                UUID id
        );

        boolean existsByOrganizationAndName(
                Organization organization,
                String name
        );
        Page<Workflow> findByOrganization(
                Organization organization,
                Pageable pageable
        );

        Page<Workflow> findByOrganizationAndStatus(
                Organization organization,
                WorkflowStatus status,
                Pageable pageable
        );

}