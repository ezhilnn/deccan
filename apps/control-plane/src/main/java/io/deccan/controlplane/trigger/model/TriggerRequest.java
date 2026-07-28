package io.deccan.controlplane.trigger.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.deccan.controlplane.trigger.enums.TriggerType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TriggerRequest {

    private UUID workflowId;

    private TriggerType triggerType;

    private JsonNode payload;

}