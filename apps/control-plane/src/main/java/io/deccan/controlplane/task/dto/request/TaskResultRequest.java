package io.deccan.controlplane.task.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResultRequest {

    private boolean success;

    private String errorMessage;

}