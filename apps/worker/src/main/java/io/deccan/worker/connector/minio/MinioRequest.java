package io.deccan.worker.connector.minio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinioRequest {

    private String operation;

    private String bucket;

    private String object;

    private String contentType;

    /**
     * Variable reference.
     *
     * Example:
     *
     * {{task.output}}
     */
    private String content;

}