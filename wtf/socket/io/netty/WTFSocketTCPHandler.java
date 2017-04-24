package wtf.socket.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import wtf.socket.exception.WTFSocketFatalException;
import wtf.socket.io.term.impl.WTFSocketDefaultTerm;
import wtf.socket.schedule.WTFSocketScheduler;
import wtf.socket.protocol.WTFSocketConnectType;
import wtf.socket.routing.WTFSocketRoutingMap;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.Arrays;

public class WTFSocketTCPHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        WTFSocketRoutingMap.TMP.register(
                new WTFSocketDefaultTerm() {{
                    setChannel(ctx.channel());
                    setConnectType(WTFSocketConnectType.TCP);
                    setIoTag(ctx.channel().id().asShortText());
                }});
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Arrays.stream(WTFSocketRoutingMap.values())
                .filter(map -> map.contains(ctx.channel().id().asShortText()))
                .forEach(map -> {
                    final WTFSocketRoutingItem item = map.getItem(ctx.channel().id().asShortText());
                    map.unRegister(item);
                    WTFSocketScheduler.getDisconnectListener().invoke(item);
                });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            WTFSocketScheduler.submit((String) msg, ctx.channel().id().asShortText(), WTFSocketConnectType.TCP);
        } catch (WTFSocketFatalException e) {
            System.out.println(e.getMessage());
            ByteBuf byteBuf = Unpooled.copiedBuffer((e.getMessage() + "\r\n").getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
