package wtf.socket.io.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;

import javax.annotation.Resource;

/**
 * Netty TCP服务函数
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
@Scope("prototype")
public class WTFSocketTCPHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private WTFSocketServer context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context.getRouting().register(
                new WTFSocketDefaultIOTerm() {{
                    setChannel(ctx.channel());
                    setConnectType("TCP");
                    setIoTag(ctx.channel().remoteAddress().toString());
                }});
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
        context.getWorkflow().submit((String) msg, ctx.channel().remoteAddress().toString(), "TCP");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
