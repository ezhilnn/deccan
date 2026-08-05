package io.deccan.controlplane.workflow.nodecatalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.deccan.controlplane.workflow.nodecatalog.entity.NodeCatalog;
import io.deccan.controlplane.workflow.nodecatalog.repository.NodeCatalogRepository;
import lombok.RequiredArgsConstructor;
import io.deccan.controlplane.workflow.nodecatalog.enums.ExecutionMode;

@Component
@RequiredArgsConstructor
public class NodeCatalogBootstrap implements CommandLineRunner {

    private final NodeCatalogRepository repository;

    @Override
    public void run(String... args) {

        register(
        "manual-trigger",
        "Manual Trigger",
        "TRIGGER",
        "manual-trigger",
        ExecutionMode.CONTROL_PLANE);
        register(
        "http",
        "HTTP Request",
        "CONNECTOR",
        "http",
        ExecutionMode.WORKER);
        register(
        "condition",
        "Condition",
        "FLOW",
        "condition",
        ExecutionMode.CONTROL_PLANE);
        register(
        "llm",
        "LLM",
        "AI",
        "llm",
        ExecutionMode.WORKER);
        register(
        "response",
        "Response",
        "FLOW",
        "response",
        ExecutionMode.CONTROL_PLANE);

    }

    private void register(
        String name,
        String display,
        String category,
        String implementation,
        ExecutionMode executionMode){

        if(repository.existsByName(name)){
            return;
        }

        NodeCatalog node=new NodeCatalog();

        node.setName(name);
        node.setDisplayName(display);
        node.setCategory(category);
        node.setImplementation(implementation);
        node.setEnabled(true);
        node.setExecutionMode(
        executionMode);
        repository.save(node);

    }

}