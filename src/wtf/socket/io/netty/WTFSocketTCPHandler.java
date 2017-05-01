package wtf.socket.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketFatalException;
import wtf.socket.exception.normal.WTFSocketNormalException;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.routing.item.WTFSocketRoutingItem;

/**
 * Netty TCP服务函数
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public class WTFSocketTCPHandler extends ChannelInboundHandlerAdapter {

    private static final Log logger = LogFactory.getLog(WTFSocketTCPHandler.class);
    private final WTFSocketServer context;

    public WTFSocketTCPHandler(WTFSocketServer context) {
        this.context = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            context.getRouting().register(
                    new WTFSocketDefaultIOTerm() {{
                        setChannel(ctx.channel());
                        setConnectType("TCP");
                        setIoTag(ctx.channel().remoteAddress().toString());
                    }});
        } catch (WTFSocketNormalException e) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer((e.getMessage() + context.getConfig().getEOTs().get(0)).getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        context.getRouting().unRegister(
                new WTFSocketDefaultIOTerm() {{
                    setIoTag(ctx.channel().remoteAddress().toString());
                }});
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            context.getScheduler().submit((String) msg, ctx.channel().remoteAddress().toString(), "TCP");
        } catch (WTFSocketFatalException e) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer((e.getMessage() + context.getConfig().getEOTs().get(0)).getBytes());
            ctx.writeAndFlush(byteBuf);
            final WTFSocketRoutingItem item = context.getRouting().getItem(ctx.channel().remoteAddress().toString());
            if (item != null)
                item.close();
            else
                ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof WTFSocketException) {
            final ByteBuf byteBuf = Unpooled.copiedBuffer((cause.getMessage() + context.getConfig().getEOTs().get(0)).getBytes());
            ctx.writeAndFlush(byteBuf);
            logger.error(cause.getMessage());
        } else {
            logger.error(cause.getClass().getSimpleName() + ": ", cause);
        }
        final WTFSocketRoutingItem item = context.getRouting().getItem(ctx.channel().remoteAddress().toString());
        if (item != null)
            item.close();
        else
            ctx.close();
    }
}
