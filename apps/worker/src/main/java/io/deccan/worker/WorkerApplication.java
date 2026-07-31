package io.deccan.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.deccan.worker.config.ControlPlaneProperties;
import io.deccan.worker.config.WorkerProperties;
import io.deccan.worker.execution.ExecutionProperties;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {
        WorkerProperties.class,
        ControlPlaneProperties.class,
        ExecutionProperties.class
})
public class WorkerApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                WorkerApplication.class,
                args);

    }

}