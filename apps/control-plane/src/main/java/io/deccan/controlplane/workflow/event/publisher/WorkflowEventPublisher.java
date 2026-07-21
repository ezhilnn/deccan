package io.deccan.controlplane.workflow.event.publisher;

import io.deccan.controlplane.workflow.event.model.WorkflowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowEventPublisher {

    private static final String TOPIC =
            "workflow-events";

    private final KafkaTemplate<String, WorkflowEvent> kafkaTemplate;

    public void publish(
            WorkflowEvent event){

        kafkaTemplate.send(
                TOPIC,
                event.getWorkflowId().toString(),
                event);

        log.info(
                "Published workflow event {}",
                event);

    }

}