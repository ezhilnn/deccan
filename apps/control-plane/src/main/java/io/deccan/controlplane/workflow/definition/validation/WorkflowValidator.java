package io.deccan.controlplane.workflow.definition.validation;

import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import org.springframework.stereotype.Component;

@Component
public class WorkflowValidator {

    public void validate(WorkflowDefinition definition) {

        if (definition == null) {
            throw new IllegalArgumentException("Workflow definition is required");
        }

        if (definition.getTrigger() == null) {
            throw new IllegalArgumentException("Workflow trigger is required");
        }

        if (definition.getNodes() == null || definition.getNodes().isEmpty()) {
            throw new IllegalArgumentException("Workflow must contain at least one node");
        }

    }

}