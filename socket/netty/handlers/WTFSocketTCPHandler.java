package wtf.socket.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import wtf.socket.main.WTFSocket;
import wtf.socket.protocols.templates.WTFSocketConnectType;

public class WTFSocketTCPHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WTFSocket.submit(ctx, (String) msg, WTFSocketConnectType.TCP);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
