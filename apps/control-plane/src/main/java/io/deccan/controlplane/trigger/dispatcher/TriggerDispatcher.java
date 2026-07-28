package io.deccan.controlplane.trigger.dispatcher;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.service.ExecutionService;
import io.deccan.controlplane.trigger.model.TriggerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TriggerDispatcher {

    private final ExecutionService executionService;

    public WorkflowExecution dispatch(
            TriggerRequest request) {

        return executionService.executeWorkflow(
                request.getWorkflowId(),
                request.getPayload());

    }

}