package io.deccan.controlplane.execution.graph;

import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkflowGraph {

    public WorkflowNode findStartNode(
            WorkflowDefinition definition) {

        Set<String> targets =
                definition.getEdges()
                        .stream()
                        .map(WorkflowEdge::getTarget)
                        .collect(Collectors.toSet());

        return definition.getNodes()
                .stream()
                .filter(node -> !targets.contains(node.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No start node found"));

    }

    public List<WorkflowNode> nextNodes(

            WorkflowDefinition definition,

            WorkflowNode current) {

        Map<String, WorkflowNode> nodes =
                definition.getNodes()
                        .stream()
                        .collect(Collectors.toMap(
                                WorkflowNode::getId,
                                n -> n));

        List<WorkflowNode> result =
                new ArrayList<>();

        for (WorkflowEdge edge :
                definition.getEdges()) {

            if (!edge.getSource()
                    .equals(current.getId())) {

                continue;

            }

            WorkflowNode next =
                    nodes.get(edge.getTarget());

            if (next != null) {

                result.add(next);

            }

        }

        return result;

    }

}