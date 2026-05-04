package org.newosrs.protocol;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.newosrs.protocol.login.LoginRequest;
import org.newosrs.service.LoginService;

final class LoginServiceTest {
    private final LoginService loginService = new LoginService();

    @Test
    void acceptsValidCredentials() {
        assertTrue(loginService.authenticate(new LoginRequest("player1", "hunter2")));
    }

    @Test
    void rejectsBlankUsername() {
        assertFalse(loginService.authenticate(new LoginRequest("   ", "hunter2")));
    }

    @Test
    void rejectsOverlengthUsername() {
        assertFalse(loginService.authenticate(new LoginRequest("averylongplayername", "hunter2")));
    }

    @Test
    void rejectsShortPassword() {
        assertFalse(loginService.authenticate(new LoginRequest("player1", "12345")));
    }
}
