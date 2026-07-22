package io.deccan.controlplane.execution.retry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetryPolicy {

    private int maxAttempts;

    private long delaySeconds;

    private boolean exponentialBackoff;

}