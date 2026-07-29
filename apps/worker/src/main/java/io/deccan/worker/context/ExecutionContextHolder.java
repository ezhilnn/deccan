package io.deccan.worker.context;

import org.springframework.stereotype.Component;

@Component
public class ExecutionContextHolder {

    private final ThreadLocal<ExecutionContext>
            context =
            ThreadLocal.withInitial(
                    ExecutionContext::new);

    public ExecutionContext get(){

        return context.get();

    }

    public void clear(){

        context.remove();

    }

}