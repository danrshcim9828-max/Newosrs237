package org.newosrs.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.CorruptedFrameException;
import org.newosrs.protocol.LoginResponseCode;
import org.newosrs.protocol.ProtocolOpcodes;
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
            switch (session.state()) {
                case HANDSHAKE -> handleHandshake(ctx, in, session);
                case LOGIN -> handleLogin(ctx, in, session);
                case INGAME, CLOSED -> ctx.close();
            }
        } finally {
            in.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof CorruptedFrameException) {
            writeResponseAndClose(ctx, LoginResponseCode.LOGIN_DENIED);
            return;
        }
        ctx.close();
    }

    private void handleHandshake(ChannelHandlerContext ctx, ByteBuf in, SessionContext session) {
        if (in.readableBytes() < 3) {
            throw new CorruptedFrameException("Handshake requires opcode + 2-byte revision");
        }

        int opcode = in.readUnsignedByte();
        if (opcode != ProtocolOpcodes.HANDSHAKE) {
            throw new CorruptedFrameException("Unsupported handshake opcode: " + opcode);
        }

        LoginResponseCode response = RevisionHandshake.isSupported(in, revision)
                ? LoginResponseCode.HANDSHAKE_OK
                : LoginResponseCode.HANDSHAKE_REVISION_MISMATCH;

        if (response != LoginResponseCode.HANDSHAKE_OK) {
            session.setState(SessionState.CLOSED);
            writeResponseAndClose(ctx, response);
            return;
        }

        in.skipBytes(2);
        session.setState(SessionState.LOGIN);
        writeResponse(ctx, LoginResponseCode.HANDSHAKE_OK);
    }

    private void handleLogin(ChannelHandlerContext ctx, ByteBuf in, SessionContext session) {
        var request = LoginRequestDecoder.decode(in);
        if (loginService.authenticate(request)) {
            session.setUsername(request.username().trim());
            session.setState(SessionState.INGAME);
            writeResponse(ctx, LoginResponseCode.LOGIN_OK);
        } else {
            session.setState(SessionState.CLOSED);
            writeResponseAndClose(ctx, LoginResponseCode.LOGIN_DENIED);
        }
    }

    private void writeResponse(ChannelHandlerContext ctx, LoginResponseCode code) {
        ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{code.code()}));
    }

    private void writeResponseAndClose(ChannelHandlerContext ctx, LoginResponseCode code) {
        ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{code.code()})).addListener(f -> ctx.close());
    }
}
