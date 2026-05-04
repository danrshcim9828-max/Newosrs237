package org.newosrs.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public final class LoginChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final int revision;
    private final int handshakeOpcode;
    private final int loginOpcode;

    public LoginChannelInitializer(int revision, int handshakeOpcode, int loginOpcode) {
        this.revision = revision;
        this.handshakeOpcode = handshakeOpcode;
        this.loginOpcode = loginOpcode;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new LoginHandshakeHandler(revision, handshakeOpcode, loginOpcode));
    }
}
