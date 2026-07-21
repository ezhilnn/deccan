package io.deccan.controlplane.workflow.nodecatalog;

import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.definition.validation.WorkflowValidationException;
import io.deccan.controlplane.workflow.nodecatalog.repository.NodeCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeCatalogValidator {

    private final NodeCatalogRepository repository;

    public void validate(
            WorkflowDefinition definition) {

        for (WorkflowNode node : definition.getNodes()) {

            if (!repository.existsByName(node.getType())) {

                throw new WorkflowValidationException(

                        "Unknown node type : "
                                + node.getType()

                );

            }

        }

    }

}