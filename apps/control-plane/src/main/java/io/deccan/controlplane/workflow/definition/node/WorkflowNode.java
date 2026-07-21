package io.deccan.controlplane.workflow.definition.node;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowNode {

    private String id;

    /**
     * Unique node type registered in node_catalog.
     * Example:
     * manual-trigger
     * http
     * llm
     * condition
     */
    private String type;

    /**
     * UI label
     */
    private String name;

    /**
     * Position in workflow builder
     */
    private Integer x;

    private Integer y;

    /**
     * Node configuration
     */
    private JsonNode configuration;

}