package io.deccan.controlplane.artifact.controller;

import io.deccan.controlplane.artifact.dto.response.ArtifactResponse;
import io.deccan.controlplane.artifact.service.ArtifactService;
import io.deccan.controlplane.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artifacts")
public class ArtifactController {

    private final ArtifactService service;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('artifact.write')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ArtifactResponse> upload(

            @RequestPart("file")
            MultipartFile file) {

        String objectName =
                service.upload(file);

        ArtifactResponse response =
                ArtifactResponse.builder()
                        .objectName(objectName)
                        .bucket(service.getBucket())
                        .url(
                                service.getObjectUrl(
                                        objectName))
                        .size(file.getSize())
                        .build();

        return ApiResponse.<ArtifactResponse>builder()
                .status(201)
                .message("Artifact uploaded successfully")
                .data(response)
                .build();

    }

    @GetMapping("/{objectName}")
    @PreAuthorize("hasAuthority('artifact.read')")
    public ResponseEntity<InputStreamResource> download(

            @PathVariable
            String objectName) {

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + objectName + "\"")
                .body(
                        new InputStreamResource(
                                service.download(objectName)));

    }

    @DeleteMapping("/{objectName}")
    @PreAuthorize("hasAuthority('artifact.write')")
    public ApiResponse<Void> delete(

            @PathVariable
            String objectName) {

        service.delete(objectName);

        return ApiResponse.<Void>builder()
                .status(200)
                .message("Artifact deleted successfully")
                .build();

    }

}