package io.deccan.controlplane.workflow.definition.node;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.workflow.definition.port.InputPort;
import io.deccan.controlplane.workflow.definition.port.OutputPort;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WorkflowNode {

    private String id;

    private String type;

    private String name;

    private Integer x;

    private Integer y;

    private JsonNode configuration;

    /**
     * Inputs consumed by this node.
     */
    private List<InputPort> inputs = new ArrayList<>();

    /**
     * Outputs produced by this node.
     */
    private List<OutputPort> outputs = new ArrayList<>();

}