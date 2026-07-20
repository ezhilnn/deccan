package io.deccan.controlplane.security.controller;

import io.deccan.controlplane.common.response.ApiResponse;
import io.deccan.controlplane.security.principal.AuthenticatedUser;
import io.deccan.controlplane.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WhoAmIController {

    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public ApiResponse<AuthenticatedUser> me() {

        return ApiResponse.<AuthenticatedUser>builder()
                .status(200)
                .message("Authenticated user")
                .data(currentUserService.currentUser())
                .build();

    }

}