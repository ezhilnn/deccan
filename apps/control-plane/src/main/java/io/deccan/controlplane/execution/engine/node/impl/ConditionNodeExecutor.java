package io.deccan.controlplane.execution.engine.node.impl;

import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.execution.context.model.NodeResult;


@Slf4j
@Component
public class ConditionNodeExecutor
        implements NodeExecutor {

    @Override
    public boolean supports(
            WorkflowNode node){

        return "condition"
                .equalsIgnoreCase(node.getType());

    }

    @Override
    public void execute(
            WorkflowNode node,
            ExecutionContext context){

        log.info(
                "Executing Condition [{}]",
                node.getId());

        boolean result =
        node.getConfiguration()
                .path("value")
                .asBoolean(true);

        context.putVariable(
                "condition",
                result);

        context.putNodeOutput(

                node.getId(),

                NodeResult.builder()
                        .success(true)
                        .data(result)
                        .build()

        );

    }

}