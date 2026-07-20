package io.deccan.controlplane.workflow.definition.validation;

import io.deccan.controlplane.workflow.definition.WorkflowDefinition;
import io.deccan.controlplane.workflow.definition.edge.WorkflowEdge;
import io.deccan.controlplane.workflow.definition.node.WorkflowNode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphValidator {

    public void validate(WorkflowDefinition definition) {

        validateNodes(definition);

        validateEdges(definition);

        validateCycles(definition);

    }

    private void validateNodes(
            WorkflowDefinition definition) {

        Set<String> ids = new HashSet<>();

        for (WorkflowNode node : definition.getNodes()) {

            if (!ids.add(node.getId())) {

                throw new WorkflowValidationException(
                        "Duplicate node id : " + node.getId());

            }

        }

    }

    private void validateEdges(
            WorkflowDefinition definition) {

        Set<String> ids =
                definition.getNodes()
                        .stream()
                        .map(WorkflowNode::getId)
                        .collect(HashSet::new,
                                HashSet::add,
                                HashSet::addAll);

        for (WorkflowEdge edge : definition.getEdges()) {

            if (!ids.contains(edge.getSource())) {

                throw new WorkflowValidationException(
                        "Unknown source node : "
                                + edge.getSource());

            }

            if (!ids.contains(edge.getTarget())) {

                throw new WorkflowValidationException(
                        "Unknown target node : "
                                + edge.getTarget());

            }

            if (edge.getSource()
                    .equals(edge.getTarget())) {

                throw new WorkflowValidationException(
                        "Self loop detected : "
                                + edge.getSource());

            }

        }

    }

    private void validateCycles(
            WorkflowDefinition definition) {

        Map<String, List<String>> graph =
                new HashMap<>();

        for (WorkflowNode node : definition.getNodes()) {

            graph.put(
                    node.getId(),
                    new ArrayList<>());

        }

        for (WorkflowEdge edge : definition.getEdges()) {

            graph.get(edge.getSource())
                    .add(edge.getTarget());

        }

        Set<String> visited =
                new HashSet<>();

        Set<String> recursion =
                new HashSet<>();

        for (String node : graph.keySet()) {

            if (dfs(
                    node,
                    graph,
                    visited,
                    recursion)) {

                throw new WorkflowValidationException(
                        "Workflow contains a cycle");

            }

        }

    }

    private boolean dfs(

            String node,

            Map<String, List<String>> graph,

            Set<String> visited,

            Set<String> recursion) {

        if (recursion.contains(node)) {

            return true;

        }

        if (visited.contains(node)) {

            return false;

        }

        visited.add(node);

        recursion.add(node);

        for (String next : graph.get(node)) {

            if (dfs(
                    next,
                    graph,
                    visited,
                    recursion)) {

                return true;

            }

        }

        recursion.remove(node);

        return false;

    }

}