package org.newosrs.net;

import io.netty.util.AttributeKey;
import org.newosrs.session.SessionContext;

public final class NetAttributes {
    public static final AttributeKey<SessionContext> SESSION = AttributeKey.valueOf("session");

    private NetAttributes() {}
}
