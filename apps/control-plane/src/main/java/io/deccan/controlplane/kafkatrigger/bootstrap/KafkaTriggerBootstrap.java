package io.deccan.controlplane.kafkatrigger.bootstrap;

import io.deccan.controlplane.kafkatrigger.registry.KafkaTriggerRegistry;
import io.deccan.controlplane.kafkatrigger.repository.WorkflowKafkaTriggerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTriggerBootstrap
        implements CommandLineRunner {

    private final WorkflowKafkaTriggerRepository repository;

    private final KafkaTriggerRegistry registry;

    @Override
    public void run(
            String... args) {

        repository.findByEnabledTrue()
                .forEach(
                        registry::register);

    }

}