package io.deccan.controlplane.execution.engine.node.impl;

import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.execution.context.model.NodeResult;


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
            WorkflowNode node,
            ExecutionContext context){

        log.info(
                "Executing HTTP node [{}]",
                node.getId());

        context.getNodeOutputs().put(
        node.getId(),
        NodeResult.builder()
                .success(true)
                .data(
                        java.util.Map.of(
                                "status",200,
                                "body","Mock HTTP response"
                        )
                )
                .build());

    }

}