package io.deccan.worker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "deccan.control-plane")
public class ControlPlaneProperties {

    private String baseUrl;

}