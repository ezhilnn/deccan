package io.deccan.controlplane.execution.engine.node;

import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.deccan.controlplane.execution.context.ExecutionContext;


@Slf4j
@Component
public class NoOpNodeExecutor
        implements NodeExecutor {

    @Override
    public boolean supports(
            WorkflowNode node){

        return false;
    }

    @Override
    public void execute(
            WorkflowNode node,
            ExecutionContext context){

        log.info(
                "Executing NOOP [{}]",
                node.getId());

    }

}