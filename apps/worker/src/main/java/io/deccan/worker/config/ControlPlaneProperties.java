package io.deccan.worker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "deccan.control-plane")
public class ControlPlaneProperties {

    private String baseUrl;

    private String email;

    private String password;

}