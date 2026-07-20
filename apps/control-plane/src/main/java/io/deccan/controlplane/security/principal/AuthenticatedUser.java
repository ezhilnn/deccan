package io.deccan.controlplane.security.principal;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class AuthenticatedUser implements Serializable {

    private UUID userId;

    private UUID organizationId;

    private String email;

    private Set<String> roles;

    private Set<String> permissions;

}