package io.deccan.controlplane.kafkatrigger.registry;

import io.deccan.controlplane.kafkatrigger.entity.WorkflowKafkaTrigger;
import io.deccan.controlplane.kafkatrigger.listener.DynamicKafkaListenerManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTriggerRegistry {

    private final DynamicKafkaListenerManager listenerManager;

    private final Set<String> topics =
            ConcurrentHashMap.newKeySet();

    public void register(
            WorkflowKafkaTrigger trigger) {

        if (topics.contains(
                trigger.getTopic())) {

            return;

        }

        topics.add(
                trigger.getTopic());

        listenerManager.register(
                trigger.getTopic());

        log.info(
                "Registered Kafka topic [{}]",
                trigger.getTopic());

    }

    public void unregister(
            String topic) {

        if (!topics.contains(
                topic)) {

            return;

        }

        topics.remove(
                topic);

        listenerManager.unregister(
                topic);

        log.info(
                "Unregistered Kafka topic [{}]",
                topic);

    }

    public boolean supports(
            String topic) {

        return topics.contains(
                topic);

    }

}