package io.deccan.controlplane.execution.routing;

import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExecutionRouter {

    public WorkflowNode selectNextNode(

            WorkflowNode current,

            List<WorkflowNode> candidates,

            ExecutionContext context){

        if(candidates.isEmpty()){

            return null;

        }

        /*
         * Temporary implementation.
         *
         * Later:
         * - Condition nodes
         * - Parallel nodes
         * - Switch nodes
         * - Loops
         */

        return candidates.get(0);

    }

}