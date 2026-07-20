package io.deccan.controlplane.security.service;

import io.deccan.controlplane.security.dto.LoginRequest;
import io.deccan.controlplane.security.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}