package io.deccan.controlplane.workflow.expression.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputBinding {

    /**
     * Input port name.
     */
    private String input;

    /**
     * Expression type.
     */
    private ExpressionType type;

    /**
     * Example:
     * {{http1.response.body}}
     * {{variables.customerId}}
     * {{secrets.openai}}
     */
    private String expression;

}