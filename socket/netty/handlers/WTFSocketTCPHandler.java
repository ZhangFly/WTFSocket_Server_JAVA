package wtf.socket.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import wtf.socket.main.WTFSocketServer;
import wtf.socket.protocols.templates.WTFSocketConnectType;
import wtf.socket.registry.WTFSocketRegistry;

public class WTFSocketTCPHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        WTFSocketRegistry.register("Tmp_" + ctx.channel().id(), ctx.channel(), WTFSocketConnectType.TCP, null, null);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WTFSocketServer.submit(ctx, (String) msg, WTFSocketConnectType.TCP);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
