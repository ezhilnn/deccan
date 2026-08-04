package io.deccan.controlplane.task.factory;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExecutionTaskFactory {

    public List<ExecutionTask> createTasks(

            WorkflowExecution execution,

            WorkflowDefinition definition){

        List<ExecutionTask> tasks =
                new ArrayList<>();

        for(WorkflowNode node : definition.getNodes()){
                if ("manual-trigger".equals(node.getType())) {
                        continue;
                }

                ExecutionTask task =
                        new ExecutionTask();

                task.setExecution(
                        execution);

                task.setNodeId(
                        node.getId());

                task.setNodeType(
                        node.getType());

                task.setConfiguration(
                        node.getConfiguration());

                task.setStatus(
                        TaskStatus.PENDING);

                task.setRetryCount(
                        0);


                tasks.add(task);

        }

        return tasks;

    }

}