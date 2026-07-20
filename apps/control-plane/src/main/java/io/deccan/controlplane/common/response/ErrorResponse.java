package io.deccan.controlplane.common.response;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final Instant timestamp = Instant.now();

    private int status;

    private String error;

    private String message;

    private String path;

    private List<String> validationErrors;

}