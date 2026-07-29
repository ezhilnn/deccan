package io.deccan.worker.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryExecutor {

    private final ExponentialBackoff
            backoff;

    public RetryResult execute(
            RetryPolicy policy,
            RetryableOperation operation){

        Exception lastException = null;

        for(int attempt=1;
            attempt<=policy.getMaxAttempts();
            attempt++){

            try{

                operation.execute();

                return RetryResult.builder()
                        .success(true)
                        .attempts(attempt)
                        .build();

            }
            catch(Exception ex){

                lastException = ex;

                log.warn(
                        "Retry attempt {} failed.",
                        attempt,
                        ex);

                if(attempt <
                        policy.getMaxAttempts()){

                    try{

                        Thread.sleep(
                                backoff.calculateDelay(
                                        policy,
                                        attempt));

                    }
                    catch(InterruptedException ie){

                        Thread.currentThread()
                                .interrupt();

                        break;

                    }

                }

            }

        }

        return RetryResult.builder()
                .success(false)
                .attempts(policy.getMaxAttempts())
                .exception(lastException)
                .build();

    }

    @FunctionalInterface
    public interface RetryableOperation{

        void execute()
                throws Exception;

    }

}