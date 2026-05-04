package org.newosrs.protocol;

import io.netty.buffer.ByteBuf;

public final class RevisionHandshake {
    private RevisionHandshake() {}

    public static boolean isSupported(ByteBuf inbound, int expectedRevision) {
        if (inbound.readableBytes() < 2) {
            return false;
        }
        int revision = inbound.getUnsignedShort(inbound.readerIndex());
        return revision == expectedRevision;
    }
}
