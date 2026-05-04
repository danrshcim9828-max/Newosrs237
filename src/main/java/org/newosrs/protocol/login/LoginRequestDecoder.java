package org.newosrs.protocol.login;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.CorruptedFrameException;
import java.nio.charset.StandardCharsets;

public final class LoginRequestDecoder {
    private static final int MAX_CREDENTIAL_LENGTH = 64;

    private LoginRequestDecoder() {}

    public static LoginRequest decode(ByteBuf in, int expectedOpcode) {
        if (in.readableBytes() < 3) {
            throw new CorruptedFrameException("Login packet too short");
        }

        int opcode = in.readUnsignedByte();
        if (opcode != expectedOpcode) {
            throw new CorruptedFrameException("Unsupported login opcode: " + opcode);
        }

        String username = readString(in, "username");
        String password = readString(in, "password");
        return new LoginRequest(username, password);
    }

    private static String readString(ByteBuf in, String fieldName) {
        if (in.readableBytes() < 1) {
            throw new CorruptedFrameException("Missing " + fieldName + " length");
        }
        int len = in.readUnsignedByte();
        if (len > MAX_CREDENTIAL_LENGTH) {
            throw new CorruptedFrameException(fieldName + " length exceeds " + MAX_CREDENTIAL_LENGTH);
        }
        if (in.readableBytes() < len) {
            throw new CorruptedFrameException("Insufficient bytes for " + fieldName);
        }
        byte[] data = new byte[len];
        in.readBytes(data);
        return new String(data, StandardCharsets.UTF_8);
    }
}
