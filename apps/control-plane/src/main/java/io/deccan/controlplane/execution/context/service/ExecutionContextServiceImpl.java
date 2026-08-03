package io.deccan.controlplane.execution.context.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.context.entity.ExecutionContextEntity;
import io.deccan.controlplane.execution.context.repository.ExecutionContextRepository;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExecutionContextServiceImpl
        implements ExecutionContextService {

    private final ExecutionContextRepository repository;

    @Override
    public void saveNodeOutput(
            WorkflowExecution execution,
            String nodeId,
            JsonNode output) {

        ExecutionContextEntity entity =
                repository
                        .findByExecutionAndNodeId(
                                execution,
                                nodeId)
                        .orElseGet(
                                ExecutionContextEntity::new);

        entity.setExecution(
                execution);

        entity.setNodeId(
                nodeId);

        entity.setOutput(
                output);

        repository.save(
                entity);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ExecutionContextEntity> getContext(
            WorkflowExecution execution) {

        return repository.findByExecution(
                execution);

    }

}