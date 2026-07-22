package io.deccan.controlplane.execution.event;

import io.deccan.controlplane.execution.event.model.ExecutionEvent;

public interface ExecutionEventPublisher {

    void publish(
            ExecutionEvent event);

}