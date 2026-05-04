package org.newosrs.protocol;

/**
 * Protocol profile used by the login service.
 * This project currently ships a local rev237 profile and does not bundle RSProt/RuneLite generated mappings.
 */
public record ProtocolProfile(int revision, int handshakeOpcode, int loginInitOpcode) {
    public static ProtocolProfile localRev237() {
        return new ProtocolProfile(237, 15, 16);
    }
}
