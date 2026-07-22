package io.deccan.controlplane.execution.event;

import io.deccan.controlplane.execution.event.model.ExecutionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaExecutionEventPublisher
        implements ExecutionEventPublisher {

    private static final String TOPIC =
            "deccan.execution.events";

    private final KafkaTemplate<String,Object>
            kafkaTemplate;

    @Override
    public void publish(
            ExecutionEvent event){

        kafkaTemplate.send(
                TOPIC,
                event.getExecutionId().toString(),
                event);

        log.info(
                "Published execution event [{}]",
                event.getType());

    }

}