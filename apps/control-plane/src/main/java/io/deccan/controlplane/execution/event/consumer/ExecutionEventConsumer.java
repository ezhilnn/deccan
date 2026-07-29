package io.deccan.controlplane.execution.event.consumer;

import io.deccan.controlplane.execution.event.constants.EventTopics;
import io.deccan.controlplane.execution.event.model.ExecutionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExecutionEventConsumer {

    @KafkaListener(
        topics = EventTopics.EXECUTION_EVENTS,
                groupId = "deccan-control-plane",
                containerFactory = "executionEventKafkaListenerContainerFactory")
        public void consume(
                ExecutionEvent event) {

        log.info(
                "Received execution event [{}] for execution [{}]",
                event.getType(),
                event.getExecutionId());

        }

}