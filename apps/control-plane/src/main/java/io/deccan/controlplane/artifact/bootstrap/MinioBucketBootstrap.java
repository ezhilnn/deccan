package io.deccan.controlplane.artifact.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.deccan.controlplane.artifact.config.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioBucketBootstrap
        implements ApplicationRunner {

    private final MinioClient minioClient;

    private final MinioProperties properties;

    @Override
    public void run(
            ApplicationArguments args)
            throws Exception {

        boolean exists =
                minioClient.bucketExists(

                        BucketExistsArgs.builder()

                                .bucket(
                                        properties.getBucket())

                                .build()

                );

        if (exists) {

            log.info(
                    "MinIO bucket [{}] already exists.",
                    properties.getBucket());

            return;

        }

        minioClient.makeBucket(

                MakeBucketArgs.builder()

                        .bucket(
                                properties.getBucket())

                        .build()

        );

        log.info(
                "Created MinIO bucket [{}].",
                properties.getBucket());

    }

}