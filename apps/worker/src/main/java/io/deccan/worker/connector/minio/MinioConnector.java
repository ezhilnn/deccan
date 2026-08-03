package io.deccan.worker.connector.minio;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deccan.worker.connector.Connector;
import io.deccan.worker.connector.ConnectorResult;
import io.deccan.worker.context.VariableResolver;
import io.deccan.worker.dto.response.ExecutionTaskResponse;
import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioConnector
        implements Connector {

    private final MinioClient minioClient;

    private final ObjectMapper objectMapper;

    private final VariableResolver variableResolver;

    @Override
    public String type() {

        return "minio";

    }

    @Override
    public ConnectorResult execute(
            ExecutionTaskResponse task) {

        try {

            MinioRequest request =
                    objectMapper.treeToValue(
                            task.getConfiguration(),
                            MinioRequest.class);

            String operation =
                    request.getOperation()
                            .toLowerCase();

            if ("upload".equals(operation)) {

                return upload(request);

            }

            if ("download".equals(operation)) {

                return download(request);

            }

            throw new IllegalArgumentException(
                    "Unsupported MinIO operation: "
                            + operation);

        }
        catch (Exception ex) {

            log.error(
                    "MinIO connector failed.",
                    ex);

            return ConnectorResult.builder()
                    .success(false)
                    .errorMessage(ex.getMessage())
                    .build();

        }

    }

    private ConnectorResult upload(
            MinioRequest request)
            throws Exception {

        String content =
                variableResolver.resolve(
                        request.getContent());

        byte[] bytes =
                content.getBytes();

        InputStream input =
                new ByteArrayInputStream(bytes);

        minioClient.putObject(

                PutObjectArgs.builder()

                        .bucket(request.getBucket())

                        .object(request.getObject())

                        .stream(
                                input,
                                bytes.length,
                                -1)

                        .contentType(
                                request.getContentType())

                        .build()

        );

        MinioResponse response =
                MinioResponse.builder()

                        .bucket(request.getBucket())

                        .object(request.getObject())

                        .size((long) bytes.length)

                        .url("/"
                                + request.getBucket()
                                + "/"
                                + request.getObject())

                        .build();

        return ConnectorResult.builder()

                .success(true)

                .output(
                        objectMapper.valueToTree(
                                response))

                .build();

    }

    private ConnectorResult download(
            MinioRequest request)
            throws Exception {

        minioClient.getObject(

                GetObjectArgs.builder()

                        .bucket(request.getBucket())

                        .object(request.getObject())

                        .build()

        ).close();

        MinioResponse response =
                MinioResponse.builder()

                        .bucket(request.getBucket())

                        .object(request.getObject())

                        .url("/"
                                + request.getBucket()
                                + "/"
                                + request.getObject())

                        .build();

        return ConnectorResult.builder()

                .success(true)

                .output(
                        objectMapper.valueToTree(
                                response))

                .build();

    }

}