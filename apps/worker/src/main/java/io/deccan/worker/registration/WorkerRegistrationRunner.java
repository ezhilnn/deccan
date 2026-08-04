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

                long deadline =
                        System.currentTimeMillis() + 60_000;

                while (true) {

                        try {

                                WorkerResponse worker =
                                        registrationService.register();

                                log.info("Worker registered successfully.");
                                log.info("Worker Id : {}", worker.getId());
                                log.info("Worker Name : {}", worker.getWorkerName());
                                log.info("----------------------------------------");

                                return;

                        } catch (Exception ex) {

                                if (System.currentTimeMillis() >= deadline) {

                                        log.error(
                                                "Unable to register worker after waiting 60 seconds.",
                                                ex);

                                        throw ex;

                                }

                                log.info(
                                        "Control Plane not ready. Retrying in 5 seconds...");

                                try {

                                        Thread.sleep(5000);

                                } catch (InterruptedException interruptedException) {

                                        Thread.currentThread().interrupt();

                                        throw new IllegalStateException(
                                                "Worker registration interrupted",
                                                interruptedException);

                                }

                        }

                }

        }

}