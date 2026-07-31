package io.deccan.controlplane.execution.engine.node.impl;

import io.deccan.controlplane.execution.engine.node.NodeExecutor;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import io.deccan.controlplane.execution.connector.ConnectorRequest;
import io.deccan.controlplane.execution.connector.ConnectorRuntime;
import io.deccan.controlplane.execution.connector.constants.ConnectorNames;
import io.deccan.controlplane.execution.connector.resolver.ConnectorRuntimeResolver;
import io.deccan.controlplane.execution.context.ExecutionContext;
import io.deccan.controlplane.execution.context.model.NodeResult;
import io.deccan.controlplane.execution.connector.ConnectorResponse;


@Slf4j
@Component
@RequiredArgsConstructor

public class HttpNodeExecutor
        implements NodeExecutor {
    private final ConnectorRuntimeResolver connectorRuntimeResolver;
    @Override
    public boolean supports(
            WorkflowNode node){

        return "http"
                .equalsIgnoreCase(node.getType());

    }

    @Override
    public void execute(
            WorkflowNode node,
            ExecutionContext context){

        log.info(
                "Executing HTTP node [{}]",
                node.getId());

        ConnectorRuntime runtime =
        connectorRuntimeResolver.resolve(
        ConnectorNames.HTTP);

            ConnectorResponse response =
                    runtime.execute(

                            ConnectorRequest.builder()

                                    .connector("http")

                                    .configuration(
                                            node.getConfiguration())

                                    .build()

                    );
                if(!response.isSuccess()){

                throw new IllegalStateException(

                        response.getError()

                );

                }

                context.putNodeOutput(

                        node.getId(),

                        NodeResult.builder()

                                .success(
                                        response.isSuccess())

                                .data(
                                        response.getBody())

                                .error(
                                        response.getError())

                                .build()

                );

    }

}