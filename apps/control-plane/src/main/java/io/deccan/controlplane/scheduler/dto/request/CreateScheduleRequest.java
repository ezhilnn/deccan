package io.deccan.controlplane.scheduler.dto.request;

import io.deccan.controlplane.scheduler.enums.ScheduleType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateScheduleRequest {

    @NotNull
    private ScheduleType type;

    private String cronExpression;

    private Boolean enabled = true;

}