package io.deccan.controlplane.workflow.definition.trigger;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowTrigger {

    private String type;

    private JsonNode configuration;

}