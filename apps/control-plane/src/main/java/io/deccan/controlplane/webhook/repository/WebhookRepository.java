package io.deccan.controlplane.webhook.repository;

import io.deccan.controlplane.webhook.entity.Webhook;
import io.deccan.controlplane.workflow.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WebhookRepository
        extends JpaRepository<Webhook, UUID> {

    Optional<Webhook> findByEndpoint(
            String endpoint);

    List<Webhook> findByWorkflow(
            Workflow workflow);

    boolean existsByEndpoint(
            String endpoint);

}