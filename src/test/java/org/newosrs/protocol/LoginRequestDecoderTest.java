package org.newosrs.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.CorruptedFrameException;
import org.junit.jupiter.api.Test;
import org.newosrs.protocol.login.LoginRequestDecoder;

final class LoginRequestDecoderTest {
    @Test
    void decodesLengthPrefixedCredentials() {
        var buf = Unpooled.buffer();
        buf.writeByte(16);
        buf.writeByte(5).writeBytes("alice".getBytes());
        buf.writeByte(8).writeBytes("s3cr3t!!".getBytes());

        var req = LoginRequestDecoder.decode(buf, 16);
        assertEquals("alice", req.username());
        assertEquals("s3cr3t!!", req.password());
    }

    @Test
    void rejectsInvalidOpcode() {
        var buf = Unpooled.buffer();
        buf.writeByte(99).writeByte(0).writeByte(0);

        assertThrows(CorruptedFrameException.class, () -> LoginRequestDecoder.decode(buf, 16));
    }

    @Test
    void rejectsMissingFieldBytes() {
        var buf = Unpooled.buffer();
        buf.writeByte(16);
        buf.writeByte(5).writeBytes("abc".getBytes());

        assertThrows(CorruptedFrameException.class, () -> LoginRequestDecoder.decode(buf, 16));
    }

    @Test
    void rejectsOverlongField() {
        var buf = Unpooled.buffer();
        buf.writeByte(16);
        buf.writeByte(65);

        assertThrows(CorruptedFrameException.class, () -> LoginRequestDecoder.decode(buf, 16));
    }
}
