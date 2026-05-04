package org.newosrs.protocol;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.newosrs.protocol.login.LoginRequestDecoder;

public class LoginRequestDecoderTest {
    @Test
    void decodesUsernameAndPassword() {
        var buf = Unpooled.buffer();
        buf.writeByte(4).writeBytes("john".getBytes());
        buf.writeByte(6).writeBytes("secret".getBytes());
        var req = LoginRequestDecoder.decode(buf);
        Assertions.assertEquals("john", req.username());
        Assertions.assertEquals("secret", req.password());
    }
}
