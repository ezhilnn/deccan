package io.deccan.controlplane.kafkatrigger.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.kafkatrigger.service.WorkflowKafkaTriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicKafkaListenerManager {

    private final ConcurrentKafkaListenerContainerFactory<String,String>
            stringKafkaListenerContainerFactory;

    private final WorkflowKafkaTriggerService triggerService;

    private final ObjectMapper objectMapper;

    private final Map<String,
            ConcurrentMessageListenerContainer<String,String>>
            listeners =
            new ConcurrentHashMap<>();

    public void register(
            String topic){

        if(listeners.containsKey(topic)){
            return;
        }

        ConcurrentMessageListenerContainer<String,String>
                container =
                stringKafkaListenerContainerFactory
                        .createContainer(topic);

        container.setupMessageListener(

                (MessageListener<String,String>) record -> {

                    try{

                        JsonNode payload =
                                objectMapper.readTree(
                                        record.value());

                        triggerService.executeTrigger(
                                topic,
                                payload);

                    }
                    catch(Exception ex){

                        log.error(
                                "Failed to process Kafka message for topic [{}]",
                                topic,
                                ex);

                    }

                });

        container.start();

        listeners.put(
                topic,
                container);

        log.info(
                "Kafka listener registered for topic [{}]",
                topic);

    }

    public void unregister(
            String topic){

        ConcurrentMessageListenerContainer<String,String>
                container =
                listeners.remove(topic);

        if(container!=null){

            container.stop();

            log.info(
                    "Kafka listener removed for topic [{}]",
                    topic);

        }

    }

}