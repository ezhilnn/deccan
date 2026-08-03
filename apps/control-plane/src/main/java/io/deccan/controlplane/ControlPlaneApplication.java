package io.deccan.controlplane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.deccan.controlplane.bootstrap.config.BootstrapProperties;

import io.deccan.controlplane.security.config.JwtProperties;

@EnableConfigurationProperties({
        JwtProperties.class,
        BootstrapProperties.class
})
@EnableScheduling
@SpringBootApplication
public class ControlPlaneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlPlaneApplication.class, args);
	}

}
