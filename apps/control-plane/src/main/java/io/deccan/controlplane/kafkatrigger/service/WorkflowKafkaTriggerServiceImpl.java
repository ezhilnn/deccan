package io.deccan.controlplane.kafkatrigger.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.service.ExecutionService;
import io.deccan.controlplane.kafkatrigger.entity.WorkflowKafkaTrigger;
import io.deccan.controlplane.kafkatrigger.registry.KafkaTriggerRegistry;
import io.deccan.controlplane.kafkatrigger.repository.WorkflowKafkaTriggerRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowKafkaTriggerServiceImpl
        implements WorkflowKafkaTriggerService {

    private final WorkflowRepository workflowRepository;

    private final WorkflowKafkaTriggerRepository triggerRepository;

    private final ExecutionService executionService;
    private final KafkaTriggerRegistry registry;

    @Override
    public WorkflowKafkaTrigger registerTrigger(
            UUID workflowId,
            String topic) {

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        WorkflowKafkaTrigger trigger =
                new WorkflowKafkaTrigger();

        trigger.setWorkflow(workflow);

        trigger.setTopic(topic);

        trigger.setEnabled(true);

        trigger =  triggerRepository.save(trigger);
        registry.register(trigger);

        return trigger;

    }

    @Override
    public WorkflowExecution executeTrigger(
            String topic,
            JsonNode payload) {

        WorkflowKafkaTrigger trigger =
                triggerRepository.findByTopic(topic)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Kafka trigger not found"));

        if (!Boolean.TRUE.equals(trigger.getEnabled())) {

            throw new IllegalArgumentException(
                    "Kafka trigger is disabled");

        }

        return executionService.executeWorkflow(
                trigger.getWorkflow().getId(),
                payload);

    }
    @Override
    public void disableTrigger(
            UUID triggerId) {

        WorkflowKafkaTrigger trigger =
                triggerRepository.findById(triggerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Kafka trigger not found"));

        trigger.setEnabled(false);

        triggerRepository.save(trigger);

        registry.unregister(
                trigger.getTopic());

    }
    @Override
    public void deleteTrigger(
            UUID triggerId) {

        WorkflowKafkaTrigger trigger =
                triggerRepository.findById(triggerId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Kafka trigger not found"));

        registry.unregister(
                trigger.getTopic());

        triggerRepository.delete(trigger);

    }

}