package io.deccan.controlplane.webhook.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWebhookRequest {

    @NotBlank
    private String endpoint;

    @NotBlank
    private String secret;

    @NotNull
    private Boolean enabled;

}