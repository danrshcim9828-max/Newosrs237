package org.newosrs.protocol;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.newosrs.protocol.login.LoginRequest;
import org.newosrs.service.LoginService;

public class LoginServiceTest {
    private final LoginService service = new LoginService();

    @Test
    void acceptsNonEmptyUserAndMinPassword() {
        Assertions.assertTrue(service.authenticate(new LoginRequest("alice", "abcd")));
    }

    @Test
    void rejectsInvalidCredentials() {
        Assertions.assertFalse(service.authenticate(new LoginRequest("", "abc")));
    }
}
