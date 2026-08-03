package io.deccan.worker.connector.minio;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MinioResponse {

    private String bucket;

    private String object;

    private String url;

    private Long size;

}