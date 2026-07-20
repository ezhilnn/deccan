package io.deccan.controlplane.workflow.definition.validation;

import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowValidator {

    private final GraphValidator graphValidator;

    public void validate(
            WorkflowDefinition definition) {

        if (definition == null) {

            throw new WorkflowValidationException(
                    "Workflow definition is required");

        }

        if (definition.getTrigger() == null) {

            throw new WorkflowValidationException(
                    "Workflow trigger is required");

        }

        if (definition.getNodes() == null
                || definition.getNodes().isEmpty()) {

            throw new WorkflowValidationException(
                    "Workflow must contain at least one node");

        }

        graphValidator.validate(definition);

    }

}