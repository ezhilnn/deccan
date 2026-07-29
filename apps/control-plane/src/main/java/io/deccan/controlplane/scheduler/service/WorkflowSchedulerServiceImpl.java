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
import io.deccan.controlplane.workflow.repository.WorkflowVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.deccan.controlplane.execution.graph.ExecutionWorkflowGraph;
import io.deccan.controlplane.execution.graph.WorkflowGraphService;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;
import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowSchedulerServiceImpl
        implements WorkflowSchedulerService {

    private final WorkflowGraphService workflowGraphService;

    private final ExecutionTaskRepository
            taskRepository;
    private final WorkflowExecutionRepository executionRepository;

    private final WorkflowVersionRepository workflowVersionRepository;


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

        WorkflowExecution execution =
                executionRepository.findById(
                        completedTask.getExecution().getId())
                .orElseThrow();

        WorkflowVersion version =
                workflowVersionRepository
                        .findById(
                                execution.getWorkflow().getId())
                        .orElseThrow();

        ExecutionWorkflowGraph graph =
                workflowGraphService.buildGraph(
                        version);

        List<ExecutionTask> tasks =
                taskRepository.findByExecutionId(
                        execution.getId());

        Map<String, ExecutionTask> taskMap =
                tasks.stream()
                        .collect(Collectors.toMap(
                                ExecutionTask::getNodeId,
                                t -> t));

        List<WorkflowEdge> outgoing =
                graph.getOutgoingEdges()
                        .getOrDefault(
                                completedTask.getNodeId(),
                                List.of());

        for (WorkflowEdge edge : outgoing) {

            String childNodeId =
                    edge.getTarget();

            ExecutionTask childTask =
                    taskMap.get(childNodeId);

            boolean ready = true;

            List<WorkflowEdge> incoming =
                    graph.getIncomingEdges()
                            .getOrDefault(
                                    childNodeId,
                                    List.of());

            for (WorkflowEdge parentEdge : incoming) {

                ExecutionTask parent =
                        taskMap.get(
                                parentEdge.getSource());

                if (parent.getStatus()
                        != TaskStatus.COMPLETED) {

                    ready = false;
                    break;

                }

            }

            if (ready &&
                    childTask.getStatus()
                            == TaskStatus.PENDING) {

                childTask.setStatus(
                        TaskStatus.READY);

                taskRepository.save(
                        childTask);

            }

        }

    }

}