package io.deccan.controlplane.workflow.connector;

import io.deccan.controlplane.connector.entity.Connector;
import io.deccan.controlplane.connector.repository.ConnectorRepository;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.definition.validation.WorkflowValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowConnectorValidator {

    private final ConnectorRepository connectorRepository;

    public void validate(
            WorkflowDefinition definition) {

        for (WorkflowNode node : definition.getNodes()) {

            if (!"connector".equalsIgnoreCase(node.getType())) {
                continue;
            }

            if (node.getConfiguration() == null ||
                    node.getConfiguration().get("connector") == null) {

                throw new WorkflowValidationException(
                        "Connector name is required");
            }

            String connectorName =
                    node.getConfiguration()
                            .get("connector")
                            .asText();

            Connector connector =
                    connectorRepository
                            .findByNameAndVersion(
                                    connectorName,
                                    "1.0.0")
                            .orElseThrow(() ->
                                    new WorkflowValidationException(
                                            "Connector not found: "
                                                    + connectorName));

            if (!connector.getEnabled()) {

                throw new WorkflowValidationException(
                        "Connector is disabled: "
                                + connectorName);

            }

        }

    }

}