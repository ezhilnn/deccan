package io.deccan.controlplane.artifact.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ArtifactService {

    String upload(
            MultipartFile file);

    InputStream download(
            String objectName);

    void delete(
            String objectName);

    String getObjectUrl(
            String objectName);

}