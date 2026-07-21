package io.deccan.controlplane.workflow.definition.edge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowEdge {

    /**
     * Source node id.
     */
    private String source;

    /**
     * Source output port.
     */
    private String sourcePort;

    /**
     * Target node id.
     */
    private String target;

    /**
     * Target input port.
     */
    private String targetPort;

}