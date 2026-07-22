package io.deccan.controlplane.execution.engine.node;

import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoOpNodeExecutor
        implements NodeExecutor {

    @Override
    public boolean supports(
            WorkflowNode node){

        return true;
    }

    @Override
    public void execute(
            WorkflowNode node){

        log.info(
                "Executing node [{}] type [{}]",
                node.getId(),
                node.getType());

    }

}