package org.newosrs.protocol.login;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

public final class LoginRequestDecoder {
    private LoginRequestDecoder() {}

    public static LoginRequest decode(ByteBuf in) {
        String username = readString(in);
        String password = readString(in);
        return new LoginRequest(username, password);
    }

    private static String readString(ByteBuf in) {
        int len = in.readUnsignedByte();
        byte[] data = new byte[len];
        in.readBytes(data);
        return new String(data, StandardCharsets.UTF_8);
    }
}
