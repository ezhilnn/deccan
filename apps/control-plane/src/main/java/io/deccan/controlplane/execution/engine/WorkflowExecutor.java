package io.deccan.controlplane.execution.engine;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.execution.context.model.NodeResult;
import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.event.ExecutionEventPublisher;
import io.deccan.controlplane.execution.event.model.ExecutionEvent;
import io.deccan.controlplane.execution.graph.WorkflowGraph;
import io.deccan.controlplane.execution.node.entity.NodeExecution;
import io.deccan.controlplane.execution.node.service.NodeExecutionService;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;
import io.deccan.controlplane.execution.routing.ExecutionRouter;
import io.deccan.controlplane.execution.state.ExecutionStateMachine;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkflowExecutor {

    private final ObjectMapper objectMapper;
    private final NodeExecutionService nodeExecutionService;

    private final List<NodeExecutor> nodeExecutors;
    private final WorkflowGraph workflowGraph;
    private final ExecutionRouter executionRouter;
    private final ExecutionStateMachine executionStateMachine;
    private final WorkflowExecutionRepository executionRepository;
    private final ExecutionEventPublisher executionEventPublisher;

    public void execute(
        WorkflowExecution execution,
        WorkflowVersion version){

        try{
                // executionStateMachine.start(execution);

                // executionRepository.save(execution);

            
            WorkflowDefinition definition =
                    objectMapper.treeToValue(
                            version.getDefinition(),
                            WorkflowDefinition.class);
                ExecutionContext context =
                        ExecutionContext.builder()
                                .execution(execution)
                                .input(execution.getInput())
                                .build();

                context.clearNodeOutputs();
                context.clearVariables();
                log.info(
        "Starting workflow execution [{}]",
        execution.getId());

            WorkflowNode current =
        workflowGraph.findStartNode(
                definition);
                if(current == null){

                        throw new IllegalStateException(
                                "Workflow has no start node");

                        }

        while (current != null) {

            final WorkflowNode currentNode = current;

           NodeExecutor executor =
        nodeExecutors.stream()
                .filter(e -> e.supports(currentNode))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No executor registered for node type: "
                                        + currentNode.getType()));

                    NodeExecution nodeExecution =
                            nodeExecutionService.start(
                                    execution,
                                    currentNode);

                    try {
                        log.info(
                        "Executing node [{}] type=[{}]",
                        currentNode.getId(),
                        currentNode.getType());
                        

                        executor.execute(
                                currentNode,
                                context);

                        nodeExecutionService.complete(
                                nodeExecution,
                                context.getNodeOutput(
                        currentNode.getId()));
                        log.info(
                        "Completed node [{}]",
                        currentNode.getId());
                        log.info(
        "Workflow execution [{}] completed successfully",
        execution.getId());

                    }
                    catch (Exception ex) {

                        nodeExecutionService.fail(
                                nodeExecution,
                                ex.getMessage());

                        throw ex;

                    }

                    List<WorkflowNode> next =
                            workflowGraph.nextNodes(
                                    definition,
                                    currentNode);

                    current =
                            executionRouter.selectNextNode(
                                    currentNode,
                                    next,
                                    context);

        }
        execution.setOutput(

        objectMapper.valueToTree(
                context.getNodeOutputs())

        );
        NodeResult result =
        context.getLastNodeOutput();

        if(result != null){

        execution.setOutput(

                objectMapper.valueToTree(
                        result.getData())

        );

        }
       
                executionStateMachine.complete(
                        execution);

                executionRepository.save(
                        execution);
                        executionEventPublisher.publish(

        ExecutionEvent.builder()

                .executionId(
                        execution.getId())

                .workflowId(
                        execution.getWorkflow().getId())

                .type(
                        "EXECUTION_COMPLETED")

                .timestamp(
                        OffsetDateTime.now())

                .build()

);
        log.info(
        "Workflow execution [{}] completed successfully",
        execution.getId());

               

        }
        catch (Exception ex){

                executionStateMachine.fail(
                        execution,
                        ex.getMessage());

                executionRepository.save(
                        execution);

                executionEventPublisher.publish(

                        ExecutionEvent.builder()

                                .executionId(
                                        execution.getId())

                                .workflowId(
                                        execution.getWorkflow().getId())

                                .type(
                                        "EXECUTION_FAILED")

                                .timestamp(
                                        OffsetDateTime.now())

                                .build()

                        );
                        log.error(
                "Workflow execution [{}] failed",
                execution.getId(),
                ex);

                if(ex instanceof RuntimeException runtimeException){

                throw runtimeException;

                }

                throw new RuntimeException(
                        ex);

        }

    }

}