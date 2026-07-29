package io.deccan.worker.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

    private Instant timestamp;

    private int status;

    private String message;

    private T data;

}