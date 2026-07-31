package io.deccan.controlplane.execution.engine.node.impl;

import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.execution.context.model.NodeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseNodeExecutor
        implements NodeExecutor {
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(
            WorkflowNode node){

        return "response"
                .equalsIgnoreCase(node.getType());

    }

    @Override
    public void execute(
            WorkflowNode node,
            ExecutionContext context){

        log.info(
                "Executing Response [{}]",
                node.getId());

        context.putNodeOutput(
        node.getId(),
        NodeResult.builder()
                .success(true)
                .data("Workflow completed")
                .build());
        // context.getExecution().setOutput(

        //         objectMapper.valueToTree(

        //                 context.getNodeOutput(
        //                         node.getId())
        //                         .getData()

        //         )

        // );

    }

}