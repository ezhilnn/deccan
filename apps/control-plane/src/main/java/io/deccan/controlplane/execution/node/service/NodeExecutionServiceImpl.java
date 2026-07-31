package io.deccan.controlplane.execution.node.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.execution.node.entity.NodeExecution;
import io.deccan.controlplane.execution.node.repository.NodeExecutionRepository;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.OffsetDateTime;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class NodeExecutionServiceImpl
        implements NodeExecutionService {

    private final NodeExecutionRepository repository;
    

    private final ObjectMapper objectMapper;

    @Override
    public NodeExecution start(
            WorkflowExecution execution,
            WorkflowNode node) {

        NodeExecution entity =
                new NodeExecution();

        entity.setWorkflowExecution(execution);
        entity.setNodeId(node.getId());
        entity.setNodeType(node.getType());

        entity.setStatus(
                ExecutionStatus.RUNNING);

        entity.setStartedAt(
                OffsetDateTime.now());
        log.info(
        "Node execution started [{}]",
        node.getId());

        return repository.save(entity);

    }

    @Override
    public void complete(
            NodeExecution nodeExecution,
            Object output) {

        nodeExecution.setStatus(
                ExecutionStatus.COMPLETED);

        nodeExecution.setFinishedAt(
                OffsetDateTime.now());

        nodeExecution.setDurationMs(
                Duration.between(
                        nodeExecution.getStartedAt(),
                        nodeExecution.getFinishedAt())
                        .toMillis());

        nodeExecution.setOutput(
                objectMapper.valueToTree(output));
        log.info(
        "Node execution completed [{}] in {} ms",
        nodeExecution.getNodeId(),
        nodeExecution.getDurationMs());

        repository.save(nodeExecution);

    }

    @Override
    public void fail(
            NodeExecution nodeExecution,
            String error) {

        nodeExecution.setStatus(
                ExecutionStatus.FAILED);

        nodeExecution.setFinishedAt(
                OffsetDateTime.now());

        nodeExecution.setDurationMs(
                Duration.between(
                        nodeExecution.getStartedAt(),
                        nodeExecution.getFinishedAt())
                        .toMillis());

        nodeExecution.setErrorMessage(error);
        log.error(
        "Node execution failed [{}] : {}",
        nodeExecution.getNodeId(),
        error);

        repository.save(nodeExecution);

    }
        @Override
        @Transactional(readOnly = true)
        public NodeExecution findLatest(

                WorkflowExecution execution,

                String nodeId){

        return repository

                .findTopByWorkflowExecutionAndNodeIdOrderByStartedAtDesc(

                        execution,

                        nodeId)

                .orElse(null);

        }

}