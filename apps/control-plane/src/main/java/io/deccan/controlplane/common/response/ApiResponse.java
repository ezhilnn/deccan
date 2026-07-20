package io.deccan.controlplane.common.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private final Instant timestamp = Instant.now();

    private int status;

    private String message;

    private T data;

}