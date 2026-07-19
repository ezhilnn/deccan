package io.deccan.controlplane.identity.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationResponse {

    private UUID id;
    private String name;
    private String slug;

}