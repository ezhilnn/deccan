package io.deccan.controlplane.execution.graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import io.deccan.controlplane.workflow.entity.WorkflowVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowGraphServiceImpl
        implements WorkflowGraphService {

    private final ObjectMapper objectMapper;

    @Override
    public WorkflowGraph buildGraph(
            WorkflowVersion version) {

        try {

            WorkflowDefinition definition =
                    objectMapper.treeToValue(
                            version.getDefinition(),
                            WorkflowDefinition.class);

            Map<String, WorkflowNode> nodes =
                    new HashMap<>();

            Map<String, List<WorkflowEdge>> outgoing =
                    new HashMap<>();

            Map<String, List<WorkflowEdge>> incoming =
                    new HashMap<>();

            for (WorkflowNode node : definition.getNodes()) {

                nodes.put(
                        node.getId(),
                        node);

                outgoing.put(
                        node.getId(),
                        new ArrayList<>());

                incoming.put(
                        node.getId(),
                        new ArrayList<>());

            }

            for (WorkflowEdge edge : definition.getEdges()) {

                outgoing
                        .get(edge.getSource())
                        .add(edge);

                incoming
                        .get(edge.getTarget())
                        .add(edge);

            }

            return WorkflowGraph.builder()
                    .nodes(nodes)
                    .outgoingEdges(outgoing)
                    .incomingEdges(incoming)
                    .build();

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Unable to build workflow graph",
                    ex);

        }

    }

}