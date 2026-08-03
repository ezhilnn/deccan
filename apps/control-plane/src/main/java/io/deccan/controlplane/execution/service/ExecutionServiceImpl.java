package io.deccan.controlplane.execution.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;

import io.deccan.controlplane.execution.context.mapper.ExecutionContextMapper;
import io.deccan.controlplane.execution.context.service.ExecutionContextService;
import io.deccan.controlplane.execution.engine.WorkflowExecutor;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import io.deccan.controlplane.execution.event.ExecutionEventPublisher;
import io.deccan.controlplane.execution.event.model.ExecutionEvent;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;
import io.deccan.controlplane.execution.retry.RetryPolicy;
import io.deccan.controlplane.execution.retry.RetryPolicyService;
import io.deccan.controlplane.execution.state.ExecutionStateMachine;
import io.deccan.controlplane.scheduler.service.WorkflowSchedulerService;
import io.deccan.controlplane.task.service.ExecutionTaskService;
import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import io.deccan.controlplane.workflow.repository.WorkflowRepository;
import io.deccan.controlplane.workflow.repository.WorkflowVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.deccan.controlplane.execution.context.dto.response.ExecutionContextResponse;
import io.deccan.controlplane.execution.context.mapper.ExecutionContextMapper;
import io.deccan.controlplane.execution.context.service.ExecutionContextService;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ExecutionServiceImpl
        implements ExecutionService {

    private final WorkflowRepository workflowRepository;

    private final WorkflowExecutionRepository executionRepository;
    private final ExecutionStateMachine executionStateMachine;

    private final WorkflowVersionRepository workflowVersionRepository;
    private final ExecutionEventPublisher executionEventPublisher;

    private final RetryPolicyService retryPolicyService;
    private final ExecutionTaskService executionTaskService;
    private final WorkflowSchedulerService workflowSchedulerService;
    private final WorkflowExecutor workflowExecutor;
    private final ExecutionContextService executionContextService;

    private final ExecutionContextMapper executionContextMapper;

    @Override
    public WorkflowExecution executeWorkflow(
            UUID workflowId,
            JsonNode input){

        Workflow workflow =
                workflowRepository.findById(workflowId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Workflow not found"));
        if (executionRepository.existsByWorkflowAndStatus(
        workflow,
        ExecutionStatus.RUNNING)) {

                throw new IllegalStateException(
                        "Workflow already has a running execution");

        }

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
        executionEventPublisher.publish(

        ExecutionEvent.builder()

                .executionId(
                        execution.getId())

                .workflowId(
                        workflow.getId())

                .type(
                        "EXECUTION_STARTED")

                .timestamp(
                        OffsetDateTime.now())

                .build()

        );

        WorkflowVersion version =
                workflowVersionRepository
                        .findFirstByWorkflowOrderByVersionDesc(
                                workflow)
                        .orElseThrow(() ->
                        new IllegalStateException(
                                "No published workflow version found."));

        try{

            executionTaskService.createTasks(
                        execution,
                        version);

                workflowSchedulerService.initializeWorkflow(
                        execution,
                        version);

                workflowExecutor.execute(
                        execution,
                        version);

                executionStateMachine.complete(
                        execution);

                execution =
                        executionRepository.save(
                                execution);

                executionEventPublisher.publish(

                        ExecutionEvent.builder()

                                .executionId(
                                        execution.getId())

                                .workflowId(
                                        workflow.getId())

                                .type(
                                        "EXECUTION_COMPLETED")

                                .timestamp(
                                        OffsetDateTime.now())

                                .build()

                );

        }
        catch(Exception ex){

                        executionStateMachine.fail(
                        execution,
                        ex.getMessage());
                                 execution =
                executionRepository.save(
                        execution);
                        log.error(
        "Execution [{}] failed",
        execution.getId(),
        ex);

                executionEventPublisher.publish(

                        ExecutionEvent.builder()
                                .executionId(execution.getId())
                                .workflowId(workflow.getId())
                                .type("EXECUTION_FAILED")
                                .timestamp(OffsetDateTime.now())
                                .build()

                );

                throw ex;

        }

        // execution =
        //         executionRepository.save(
        //                 execution);
        log.info(
        "Execution [{}] created successfully",
        execution.getId());
        if(execution.getStatus()==ExecutionStatus.RUNNING){

        throw new IllegalStateException(
                "Execution finished but is still RUNNING");

        }

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

        @Override
        public void cancelExecution(
                UUID executionId) {

                WorkflowExecution execution =
                        executionRepository.findById(
                                executionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Execution not found"));

                executionStateMachine.cancel(
                        execution);
                executionTaskService.cancelTasks(
                        execution.getId());

                executionRepository.save(
                        execution);

                executionEventPublisher.publish(

                        ExecutionEvent.builder()
                                .executionId(
                                        execution.getId())
                                .workflowId(
                                        execution.getWorkflow().getId())
                                .type(
                                        "EXECUTION_CANCELLED")
                                .timestamp(
                                        OffsetDateTime.now())
                                .build()

                );

        }
        @Override
        public WorkflowExecution retryExecution(
                UUID executionId) {

                WorkflowExecution previous =
                        executionRepository.findById(
                                executionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Execution not found"));

                if (previous.getStatus() != ExecutionStatus.FAILED) {

                        throw new IllegalStateException(
                                "Only failed executions can be retried");

                }
                executionEventPublisher.publish(

                ExecutionEvent.builder()

                                .executionId(
                                        previous.getId())

                                .workflowId(
                                        previous.getWorkflow().getId())

                                .type(
                                        "EXECUTION_RETRY_REQUESTED")

                                .timestamp(
                                        OffsetDateTime.now())

                                .build()

                );
                RetryPolicy policy =
                        RetryPolicy.builder()
                                .maxAttempts(3)
                                .delaySeconds(5)
                                .exponentialBackoff(true)
                                .build();

                retryPolicyService.validateRetry(
                        previous,
                        policy);

                return executeWorkflow(

                        previous.getWorkflow().getId(),

                        previous.getInput()

                );

        }
        @Override
        @Transactional(readOnly = true)
        public ExecutionContextResponse getExecutionContext(

                UUID executionId){

        WorkflowExecution execution =

                executionRepository.findById(

                        executionId)

                .orElseThrow(() ->

                        new IllegalArgumentException(

                                "Execution not found"));

        return executionContextMapper.toResponse(

                executionContextService.getContext(

                        execution));

        }
        

}