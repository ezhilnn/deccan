package io.deccan.controlplane.execution.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.deccan.controlplane.execution.state.ExecutionStateMachine;
import io.deccan.controlplane.execution.engine.WorkflowExecutor;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import io.deccan.controlplane.workflow.repository.WorkflowVersionRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExecutionServiceImpl
        implements ExecutionService {

    private final WorkflowRepository workflowRepository;

    private final WorkflowExecutionRepository executionRepository;
    private final ExecutionStateMachine executionStateMachine;

    private final WorkflowVersionRepository workflowVersionRepository;

    private final WorkflowExecutor workflowExecutor;
    @Override
    public WorkflowExecution executeWorkflow(
            UUID workflowId,
            JsonNode input){

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        WorkflowExecution execution =
                new WorkflowExecution();

        execution.setWorkflow(workflow);

        execution.setWorkflowVersion(
                workflow.getCurrentVersion());

        execution.setStatus(
        ExecutionStatus.PENDING);

        execution.setStartedAt(
        OffsetDateTime.now());

        execution.setInput(input);

        execution = executionRepository.save(
                execution);

        executionStateMachine.start(
        execution);

        execution =
                executionRepository.save(
                        execution);

        WorkflowVersion version =
                workflowVersionRepository
                        .findFirstByWorkflowOrderByVersionDesc(
                                workflow)
                        .orElseThrow();

        try{

            workflowExecutor.execute(
                execution,
                version);

            executionStateMachine.complete(
                    execution);

        }
        catch(Exception ex){

            executionStateMachine.fail(
                    execution,
                    ex.getMessage());

        }

        execution =
                executionRepository.save(
                        execution);

        return execution;

    }

    @Override
    @Transactional(readOnly = true)
    public WorkflowExecution getExecution(
            UUID executionId){

        return executionRepository.findById(executionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Execution not found"));

    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowExecution> getExecutions(
            UUID workflowId){

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));

        return executionRepository
                .findByWorkflowOrderByStartedAtDesc(
                        workflow);

    }

}