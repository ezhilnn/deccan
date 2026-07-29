package io.deccan.worker.retry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetryResult {

    private boolean success;

    private int attempts;

    private Exception exception;

}