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

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkflowSchedulerServiceImpl
        implements WorkflowSchedulerService {

    private final ObjectMapper objectMapper;

    private final ExecutionTaskRepository
            taskRepository;

    @Override
    public void initializeWorkflow(
            WorkflowExecution execution,
            WorkflowVersion version) {

        try {

            WorkflowDefinition definition =
                    objectMapper.treeToValue(
                            version.getDefinition(),
                            WorkflowDefinition.class);

            Set<String> targetNodes =
                    new HashSet<>();

            for (WorkflowEdge edge : definition.getEdges()) {

                targetNodes.add(
                        edge.getTarget());

            }

            for (WorkflowNode node : definition.getNodes()) {

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