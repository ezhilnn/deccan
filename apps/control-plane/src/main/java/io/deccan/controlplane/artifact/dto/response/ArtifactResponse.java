package io.deccan.controlplane.artifact.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtifactResponse {

    private String objectName;

    private String bucket;

    private String url;

    private long size;

}