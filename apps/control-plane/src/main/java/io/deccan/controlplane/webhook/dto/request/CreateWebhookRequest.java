package io.deccan.controlplane.webhook.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateWebhookRequest {

    @NotNull
    private UUID workflowId;

    @NotBlank
    private String endpoint;

    @NotBlank
    private String secret;

}