package org.newosrs.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public final class LoginChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final int revision;

    public LoginChannelInitializer(int revision) {
        this.revision = revision;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new LoginHandshakeHandler(revision));
    }
}
