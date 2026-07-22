package io.deccan.controlplane.execution.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkflowExecutor {

    private final ObjectMapper objectMapper;

    private final List<NodeExecutor> nodeExecutors;

    public void execute(
            WorkflowVersion version){

        try{

            WorkflowDefinition definition =
                    objectMapper.treeToValue(
                            version.getDefinition(),
                            WorkflowDefinition.class);

            for(WorkflowNode node
                    : definition.getNodes()){

                nodeExecutors.stream()
                        .filter(e ->
                                e.supports(node))
                        .findFirst()
                        .orElseThrow()
                        .execute(node);

            }

        }
        catch (Exception ex){

            throw new RuntimeException(
                    ex);

        }

    }

}