package io.deccan.worker.context;

public interface VariableResolver {

   String resolve(
        String value);

boolean evaluate(
        String expression);

}