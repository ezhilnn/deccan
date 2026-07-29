package io.deccan.worker.retry;

import org.springframework.stereotype.Component;

@Component
public class ExponentialBackoff {

    public long calculateDelay(
            RetryPolicy policy,
            int attempt){

        double delay =
                policy.getInitialDelay()
                        * Math.pow(
                                policy.getMultiplier(),
                                attempt - 1);

        return Math.min(
                (long) delay,
                policy.getMaxDelay());

    }

}