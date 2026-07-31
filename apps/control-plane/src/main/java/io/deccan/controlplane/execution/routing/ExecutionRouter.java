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

        if(candidates == null || candidates.isEmpty()){

            return null;

        }

        if(candidates.size() == 1){

            return candidates.get(0);

        }

        switch(current.getType()){

            case "condition":

                return evaluateCondition(
                        candidates,
                        context);

            default:

                return candidates.get(0);

        }

    }
    private WorkflowNode evaluateCondition(

            List<WorkflowNode> candidates,

            ExecutionContext context){

        Object result =
                context.getVariable(
                        "condition");

        if(result instanceof Boolean value){

            if(value){

                return candidates.get(0);

            }

            if(candidates.size() > 1){

                return candidates.get(1);

            }

        }

        return candidates.get(0);

    }

}