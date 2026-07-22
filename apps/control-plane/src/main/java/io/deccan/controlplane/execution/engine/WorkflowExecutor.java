package io.deccan.controlplane.execution.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.execution.entity.WorkflowExecution;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkflowExecutor {

    private final ObjectMapper objectMapper;

    private final List<NodeExecutor> nodeExecutors;

    public void execute(
        WorkflowExecution execution,
        WorkflowVersion version){

        try{

            WorkflowDefinition definition =
                    objectMapper.treeToValue(
                            version.getDefinition(),
                            WorkflowDefinition.class);
            ExecutionContext context =
                ExecutionContext.builder()
                        .execution(execution)
                        .input(execution.getInput())
                        .build();

            for(WorkflowNode node
                    : definition.getNodes()){

                NodeExecutor executor =
                nodeExecutors.stream()
                        .filter(e -> e.supports(node))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No executor registered for node type: "
                                                + node.getType()));

            executor.execute(
            node,
            context);

            }

        }
        catch (Exception ex){

            throw new RuntimeException(
                    ex);

        }

    }

}