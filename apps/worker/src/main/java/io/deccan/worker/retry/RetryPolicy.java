package io.deccan.worker.retry;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetryPolicy {

    private int maxAttempts;

    private long initialDelay;

    private double multiplier;

    private long maxDelay;

}