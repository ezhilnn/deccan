package io.deccan.controlplane.security.service;

import io.deccan.controlplane.security.principal.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CurrentUserService {

    public AuthenticatedUser currentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null) {
            return null;
        }

        return (AuthenticatedUser) authentication.getPrincipal();

    }

    public UUID userId() {
        return currentUser().getUserId();
    }

    public UUID organizationId() {
        return currentUser().getOrganizationId();
    }

    public String email() {
        return currentUser().getEmail();
    }

}