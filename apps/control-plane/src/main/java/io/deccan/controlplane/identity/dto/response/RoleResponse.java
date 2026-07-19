package io.deccan.controlplane.identity.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleResponse {

    private UUID id;
    private String name;
    private String description;

}