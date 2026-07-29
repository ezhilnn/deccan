package io.deccan.worker.registration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.deccan.worker.dto.response.WorkerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkerRegistrationRunner
        implements ApplicationRunner {

    private final WorkerRegistrationService
            registrationService;

    @Override
    public void run(
            ApplicationArguments args) {

        log.info("----------------------------------------");
        log.info("Registering worker...");

        WorkerResponse worker =
                registrationService.register();

        log.info(
                "Worker registered successfully.");

        log.info(
                "Worker Id : {}",
                worker.getId());

        log.info(
                "Worker Name : {}",
                worker.getWorkerName());

        log.info("----------------------------------------");

    }

}