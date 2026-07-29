package io.deccan.worker.execution;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "worker.execution")
public class ExecutionProperties {

    private long timeout = 300000;

}