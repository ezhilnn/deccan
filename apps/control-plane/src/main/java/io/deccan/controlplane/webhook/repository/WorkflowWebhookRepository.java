package io.deccan.controlplane.webhook.repository;

import io.deccan.controlplane.webhook.entity.WorkflowWebhook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkflowWebhookRepository
        extends JpaRepository<WorkflowWebhook, UUID> {

    Optional<WorkflowWebhook> findByToken(
            String token);

}