package org.newosrs.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.newosrs.config.ServerConfig;

public final class GameServer {
    private final ServerConfig config;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private Channel boundChannel;

    public GameServer(ServerConfig config) {
        this.config = config;
    }

    public void start() throws InterruptedException {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new LoginChannelInitializer(config.revision(), config.handshakeOpcode(), config.loginOpcode()));

        boundChannel = bootstrap.bind(config.host(), config.port()).sync().channel();
    }

    public void stop() {
        if (boundChannel != null) {
            boundChannel.close();
        }
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
    }
}
