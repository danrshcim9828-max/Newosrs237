package org.newosrs.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.newosrs.protocol.RevisionHandshake;
import org.newosrs.protocol.login.LoginRequestDecoder;
import org.newosrs.service.LoginService;
import org.newosrs.session.SessionContext;
import org.newosrs.session.SessionState;

public final class LoginHandshakeHandler extends ChannelInboundHandlerAdapter {
    private final int revision;
    private final LoginService loginService = new LoginService();

    public LoginHandshakeHandler(int revision) {
        this.revision = revision;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().attr(NetAttributes.SESSION).set(new SessionContext(ctx.channel()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        SessionContext session = ctx.channel().attr(NetAttributes.SESSION).get();
        try {
            if (session.state() == SessionState.HANDSHAKE) {
                byte response = RevisionHandshake.isSupported(in, revision) ? (byte) 0 : (byte) 6;
                if (response != 0) {
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{response})).addListener(f -> ctx.close());
                    session.setState(SessionState.CLOSED);
                    return;
                }
                in.skipBytes(2);
                session.setState(SessionState.LOGIN);
                ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{0}));
                return;
            }

            if (session.state() == SessionState.LOGIN) {
                var request = LoginRequestDecoder.decode(in);
                if (loginService.authenticate(request)) {
                    session.setUsername(request.username());
                    session.setState(SessionState.INGAME);
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{2}));
                } else {
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{3})).addListener(f -> ctx.close());
                    session.setState(SessionState.CLOSED);
                }
            }
        } finally {
            in.release();
        }
    }
}
