package io.deccan.controlplane.execution.context.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NodeResult {

    private boolean success;

    private Object data;

    private String error;

}