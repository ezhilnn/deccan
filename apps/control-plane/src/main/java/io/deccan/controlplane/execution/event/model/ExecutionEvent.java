package io.deccan.controlplane.execution.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionEvent {

    private UUID executionId;

    private UUID workflowId;

    private String type;

    private OffsetDateTime timestamp;
    
    private String nodeId;

    private String nodeType;

    private String status;

    private String message;

}