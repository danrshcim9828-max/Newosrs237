package org.newosrs.service;

import org.newosrs.protocol.login.LoginRequest;

public final class LoginService {
    public boolean authenticate(LoginRequest request) {
        return !request.username().isBlank() && request.password().length() >= 4;
    }
}
