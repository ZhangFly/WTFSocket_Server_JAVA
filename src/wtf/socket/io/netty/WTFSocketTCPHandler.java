package wtf.socket.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocket;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketFatalException;
import wtf.socket.exception.normal.WTFSocketNormalException;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.routing.WTFSocketRouting;
import wtf.socket.routing.item.WTFSocketRoutingItem;

public class WTFSocketTCPHandler extends ChannelInboundHandlerAdapter {

    private static final Log logger = LogFactory.getLog(WTFSocketTCPHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            WTFSocket.ROUTING.register(
                    new WTFSocketDefaultIOTerm() {{
                        setChannel(ctx.channel());
                        setConnectType("TCP");
                        setIoTag(ctx.channel().remoteAddress().toString());
                    }});
        } catch (WTFSocketNormalException e) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer((e.getMessage() + "\r\n").getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        WTFSocket.ROUTING.unRegister(
                new WTFSocketDefaultIOTerm() {{
                    setIoTag(ctx.channel().remoteAddress().toString());
                }});
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            WTFSocket.SCHEDULER.submit((String) msg, ctx.channel().remoteAddress().toString(), "TCP");
        } catch (WTFSocketFatalException e) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer((e.getMessage() + "\r\n").getBytes());
            ctx.writeAndFlush(byteBuf);
            final WTFSocketRoutingItem item = WTFSocket.ROUTING.getItem(ctx.channel().remoteAddress().toString());
            if (item != null)
                item.logout();
            else
                ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof WTFSocketException) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer((cause.getMessage() + "\r\n").getBytes());
            ctx.writeAndFlush(byteBuf);
            logger.error(cause.getMessage());
        } else {
            logger.error(cause.getClass().getSimpleName() + ": ", cause);
        }
        final WTFSocketRoutingItem item = WTFSocket.ROUTING.getItem(ctx.channel().remoteAddress().toString());
        if (item != null)
            item.logout();
        else
            ctx.close();
    }
}
