package io.deccan.controlplane.task.factory;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.nodecatalog.repository.NodeCatalogRepository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

import io.deccan.controlplane.workflow.nodecatalog.entity.NodeCatalog;
import io.deccan.controlplane.workflow.nodecatalog.enums.ExecutionMode;
import io.deccan.controlplane.workflow.nodecatalog.repository.NodeCatalogRepository;


@RequiredArgsConstructor
@Component
public class ExecutionTaskFactory {
        private final NodeCatalogRepository nodeCatalogRepository;

    public List<ExecutionTask> createTasks(

            WorkflowExecution execution,

            WorkflowDefinition definition){

        List<ExecutionTask> tasks =
                new ArrayList<>();

        for(WorkflowNode node : definition.getNodes()){
                NodeCatalog catalog =
                                nodeCatalogRepository
                                        .findByName(node.getType())
                                        .orElseThrow(() ->
                                                new IllegalStateException(
                                                        "Unknown node type: " + node.getType()));

                        if (catalog.getExecutionMode()
                                == ExecutionMode.CONTROL_PLANE) {

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