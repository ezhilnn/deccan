package io.deccan.controlplane.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.task.entity.ExecutionTask;
import io.deccan.controlplane.task.enums.TaskStatus;
import io.deccan.controlplane.task.repository.ExecutionTaskRepository;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.deccan.controlplane.execution.graph.ExecutionWorkflowGraph;
import io.deccan.controlplane.execution.graph.WorkflowGraphService;
import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowSchedulerServiceImpl
        implements WorkflowSchedulerService {

    private final WorkflowGraphService workflowGraphService;

    private final ExecutionTaskRepository
            taskRepository;

    @Override
    public void initializeWorkflow(
            WorkflowExecution execution,
            WorkflowVersion version) {

        try {

            ExecutionWorkflowGraph graph =
            workflowGraphService.buildGraph(
                    version);

            Set<String> targetNodes =
                    new HashSet<>();

           for (var edges : graph.getIncomingEdges().values()) {

            for (WorkflowEdge edge : edges) {

                targetNodes.add(
                        edge.getTarget());

            }

        }

            for (WorkflowNode node :  graph.getNodes().values()) {

                if (targetNodes.contains(node.getId())) {
                    continue;
                }

                ExecutionTask task =
                        taskRepository
                                .findByExecutionIdAndNodeId(
                                        execution.getId(),
                                        node.getId())
                                .orElseThrow();

                task.setStatus(
                        TaskStatus.READY);

                taskRepository.save(task);

            }

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to initialize workflow",
                    ex);

        }

    }
    @Override
    public void scheduleNextTasks(
            ExecutionTask completedTask) {

    }

}