package io.deccan.controlplane.worker.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWorkerRequest {

    @NotBlank
    private String workerName;

    @NotBlank
    private String hostName;

    @NotNull
    private JsonNode capabilities;

}