package io.deccan.worker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "worker")
public class WorkerProperties {

    private String name;

    private String host;

    private Integer port;

    private String[] capabilities;

    private Heartbeat heartbeat = new Heartbeat();

    private Polling polling = new Polling();

    @Getter
    @Setter
    public static class Heartbeat {

        private String interval;

    }

    @Getter
    @Setter
    public static class Polling {

        private String interval;

    }

}