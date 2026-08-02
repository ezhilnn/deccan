package io.deccan.controlplane.artifact.service;

import io.deccan.controlplane.artifact.config.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtifactServiceImpl
        implements ArtifactService {

    private final MinioClient minioClient;

    private final MinioProperties properties;

    @Override
    public String upload(
            MultipartFile file) {

        try {

            String objectName =
                    UUID.randomUUID()
                            + "-"
                            + file.getOriginalFilename();

            minioClient.putObject(

                    PutObjectArgs.builder()

                            .bucket(
                                    properties.getBucket())

                            .object(
                                    objectName)

                            .stream(

                                    file.getInputStream(),

                                    file.getSize(),

                                    -1

                            )

                            .contentType(
                                    file.getContentType())

                            .build()

            );

            return objectName;

        }
        catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to upload artifact",
                    ex);

        }

    }

    @Override
    public InputStream download(
            String objectName) {

        try {

            return minioClient.getObject(

                    GetObjectArgs.builder()

                            .bucket(
                                    properties.getBucket())

                            .object(
                                    objectName)

                            .build()

            );

        }
        catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to download artifact",
                    ex);

        }

    }

    @Override
    public void delete(
            String objectName) {

        try {

            minioClient.removeObject(

                    RemoveObjectArgs.builder()

                            .bucket(
                                    properties.getBucket())

                            .object(
                                    objectName)

                            .build()

            );

        }
        catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to delete artifact",
                    ex);

        }

    }

    @Override
    public String getObjectUrl(
            String objectName) {

        try {

            return minioClient.getPresignedObjectUrl(

                    GetPresignedObjectUrlArgs.builder()

                            .method(
                                    Method.GET)

                            .bucket(
                                    properties.getBucket())

                            .object(
                                    objectName)

                            .build()

            );

        }
        catch (Exception ex) {

            throw new RuntimeException(
                    "Unable to generate artifact URL",
                    ex);

        }

    }

}git add .

git commit -m "feat: implement artifact storage service"
