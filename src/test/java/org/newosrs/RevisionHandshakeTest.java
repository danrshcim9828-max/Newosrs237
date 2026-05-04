package org.newosrs;

import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.newosrs.protocol.RevisionHandshake;

public class RevisionHandshakeTest {
    @Test
    void supportsCorrectRevision() {
        var buf = Unpooled.buffer();
        buf.writeShort(237);
        Assertions.assertTrue(RevisionHandshake.isSupported(buf, 237));
    }

    @Test
    void rejectsWrongRevision() {
        var buf = Unpooled.buffer();
        buf.writeShort(225);
        Assertions.assertFalse(RevisionHandshake.isSupported(buf, 237));
    }
}
