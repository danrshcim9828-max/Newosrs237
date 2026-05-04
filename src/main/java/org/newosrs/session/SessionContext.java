package org.newosrs.session;

import io.netty.channel.Channel;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class SessionContext {
    private final UUID id = UUID.randomUUID();
    private final Channel channel;
    private final AtomicReference<SessionState> state = new AtomicReference<>(SessionState.HANDSHAKE);
    private volatile String username = "";

    public SessionContext(Channel channel) {
        this.channel = channel;
    }

    public UUID id() { return id; }
    public Channel channel() { return channel; }
    public SessionState state() { return state.get(); }
    public void setState(SessionState next) { state.set(next); }
    public String username() { return username; }
    public void setUsername(String username) { this.username = username; }
}
