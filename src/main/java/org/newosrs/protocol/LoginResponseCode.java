package org.newosrs.protocol;

public enum LoginResponseCode {
    HANDSHAKE_OK(0),
    HANDSHAKE_REVISION_MISMATCH(6),
    LOGIN_OK(2),
    LOGIN_DENIED(3);

    private final byte code;

    LoginResponseCode(int code) {
        this.code = (byte) code;
    }

    public byte code() {
        return code;
    }
}
