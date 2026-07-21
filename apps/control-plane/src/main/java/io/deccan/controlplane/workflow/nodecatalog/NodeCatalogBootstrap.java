package io.deccan.controlplane.workflow.nodecatalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.deccan.controlplane.workflow.nodecatalog.entity.NodeCatalog;
import io.deccan.controlplane.workflow.nodecatalog.repository.NodeCatalogRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NodeCatalogBootstrap implements CommandLineRunner {

    private final NodeCatalogRepository repository;

    @Override
    public void run(String... args) {

        register("manual-trigger","Manual Trigger","TRIGGER","manual-trigger");
        register("http","HTTP Request","CONNECTOR","http");
        register("condition","Condition","FLOW","condition");
        register("llm","LLM","AI","llm");
        register("response","Response","FLOW","response");

    }

    private void register(
            String name,
            String display,
            String category,
            String implementation){

        if(repository.existsByName(name)){
            return;
        }

        NodeCatalog node=new NodeCatalog();

        node.setName(name);
        node.setDisplayName(display);
        node.setCategory(category);
        node.setImplementation(implementation);
        node.setEnabled(true);

        repository.save(node);

    }

}