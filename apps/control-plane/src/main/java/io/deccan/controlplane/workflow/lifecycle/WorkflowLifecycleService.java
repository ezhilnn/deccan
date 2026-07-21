package io.deccan.controlplane.workflow.lifecycle;

import io.deccan.controlplane.workflow.entity.Workflow;
import io.deccan.controlplane.workflow.enums.WorkflowStatus;
import org.springframework.stereotype.Component;

@Component
public class WorkflowLifecycleService {

    public void publish(
        Workflow workflow) {

    if (workflow.getStatus() != WorkflowStatus.DRAFT) {

        throw new WorkflowLifecycleException(
                "Only DRAFT workflows can be published");

    }

    workflow.setStatus(
            WorkflowStatus.ACTIVE);

}

    public void archive(
            Workflow workflow) {

        if (workflow.getStatus() != WorkflowStatus.ACTIVE) {

            throw new WorkflowLifecycleException(
                    "Only ACTIVE workflows can be archived");

        }

        workflow.setStatus(
                WorkflowStatus.ARCHIVED);

    }

    public void activate(
            Workflow workflow) {

        if (workflow.getStatus() != WorkflowStatus.ARCHIVED) {

            throw new WorkflowLifecycleException(
                    "Only ARCHIVED workflows can be activated");

        }

        workflow.setStatus(
                WorkflowStatus.ACTIVE);

    }

}