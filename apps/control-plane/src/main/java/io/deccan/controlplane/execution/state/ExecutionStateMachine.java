package io.deccan.controlplane.execution.state;

import io.deccan.controlplane.execution.entity.WorkflowExecution;
import io.deccan.controlplane.execution.enums.ExecutionStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class ExecutionStateMachine {

    public void start(
            WorkflowExecution execution){

        ensureState(
                execution,
                ExecutionStatus.PENDING
        );

        execution.setStatus(
                ExecutionStatus.RUNNING
        );

    }

    public void complete(
            WorkflowExecution execution){

        ensureState(
                execution,
                ExecutionStatus.RUNNING
        );

        execution.setStatus(
                ExecutionStatus.COMPLETED
        );

        execution.setFinishedAt(
                OffsetDateTime.now()
        );

    }

    public void fail(
            WorkflowExecution execution,
            String errorMessage){

        ensureState(
                execution,
                ExecutionStatus.RUNNING
        );

        execution.setStatus(
                ExecutionStatus.FAILED
        );

        execution.setErrorMessage(
                errorMessage
        );

        execution.setFinishedAt(
                OffsetDateTime.now()
        );

    }

    public void cancel(
            WorkflowExecution execution){

        ensureState(
            execution,
            ExecutionStatus.RUNNING);



        execution.setStatus(
                ExecutionStatus.CANCELLED
        );

        execution.setFinishedAt(
                OffsetDateTime.now()
        );

    }
    public boolean isTerminalState(
        WorkflowExecution execution){

        return execution.getStatus()==ExecutionStatus.COMPLETED

                || execution.getStatus()==ExecutionStatus.FAILED

                || execution.getStatus()==ExecutionStatus.CANCELLED;

        }
    private void ensureState(
            WorkflowExecution execution,
            ExecutionStatus expected){

        if(execution.getStatus()!=expected){

            throw new IllegalStateException(

                    "Invalid state transition. Expected "

                            + expected

                            + " but was "

                            + execution.getStatus()

            );

        }

    }

}