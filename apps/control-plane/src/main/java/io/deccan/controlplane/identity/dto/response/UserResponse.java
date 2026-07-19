package io.deccan.controlplane.identity.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

}