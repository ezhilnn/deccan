package io.deccan.controlplane.workflow.definition.node;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowNode {

    private String id;

    private String type;

    private String connector;

    private JsonNode configuration;

}