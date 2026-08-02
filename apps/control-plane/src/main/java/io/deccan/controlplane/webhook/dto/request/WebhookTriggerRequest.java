package io.deccan.controlplane.webhook.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookTriggerRequest {

    private String secret;

    private JsonNode payload;

}