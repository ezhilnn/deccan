package io.deccan.controlplane.execution.retry;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.repository.WorkflowExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetryPolicyService {

    private final WorkflowExecutionRepository repository;

    public void validateRetry(
            WorkflowExecution execution,
            RetryPolicy policy) {

        long attempts =
                repository.findByWorkflowOrderByStartedAtDesc(
                                execution.getWorkflow())
                        .stream()
                        .filter(e ->
                                e.getStatus().name().equals("FAILED"))
                        .count();

        if (attempts >= policy.getMaxAttempts()) {

            throw new IllegalStateException(
                    "Maximum retry attempts exceeded");

        }

    }

}