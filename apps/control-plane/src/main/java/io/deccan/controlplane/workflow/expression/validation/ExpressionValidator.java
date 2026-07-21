package io.deccan.controlplane.workflow.expression.validation;

import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.expression.model.InputBinding;
import io.deccan.controlplane.workflow.definition.validation.WorkflowValidationException;
import org.springframework.stereotype.Component;

@Component
public class ExpressionValidator {

    public void validate(
            WorkflowDefinition definition) {

        for (WorkflowNode node : definition.getNodes()) {

            for (InputBinding binding : node.getBindings()) {

                if (binding.getExpression() == null ||
                        binding.getExpression().isBlank()) {

                    throw new WorkflowValidationException(
                            "Binding expression cannot be empty");

                }

            }

        }

    }

}