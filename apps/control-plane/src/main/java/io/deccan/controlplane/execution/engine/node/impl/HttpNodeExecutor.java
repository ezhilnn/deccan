package io.deccan.controlplane.execution.engine.node.impl;

import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpNodeExecutor
        implements NodeExecutor {

    @Override
    public boolean supports(
            WorkflowNode node){

        return "http"
                .equalsIgnoreCase(node.getType());

    }

    @Override
    public void execute(
            WorkflowNode node){

        log.info(
                "Executing HTTP node [{}]",
                node.getId());

    }

}