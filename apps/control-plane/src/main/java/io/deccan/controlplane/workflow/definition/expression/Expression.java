package io.deccan.controlplane.workflow.definition.expression;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Expression {

    /**
     * Example:
     * {{trigger.body.email}}
     * {{http1.response.status}}
     */
    private String expression;

}