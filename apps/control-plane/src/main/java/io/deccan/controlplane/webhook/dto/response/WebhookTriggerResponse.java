package io.deccan.controlplane.webhook.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebhookTriggerResponse {

    private boolean accepted;

    private String message;

}