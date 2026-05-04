package org.newosrs.service;

import org.newosrs.protocol.login.LoginRequest;

public final class LoginService {
    private static final int MIN_PASSWORD_LENGTH = 6;

    public boolean authenticate(LoginRequest request) {
        String normalizedUser = request.username().trim();
        return !normalizedUser.isEmpty()
                && normalizedUser.length() <= 12
                && request.password().length() >= MIN_PASSWORD_LENGTH;
    }
}
