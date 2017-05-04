package wtf.socket.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;

import javax.annotation.Resource;

/**
 * Netty TCP初始化函数
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public class WTFSocketTCPInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private WTFSocketServer context;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        final ByteBuf delimiters[] = new ByteBuf[context.getConfig().getEOTs().size()];
        for (int i = 0; i < context.getConfig().getEOTs().size(); i++) {
            delimiters[i] = Unpooled.copiedBuffer(context.getConfig().getEOTs().get(i).getBytes());
        }
        pipeline.addLast(new DelimiterBasedFrameDecoder(65536, delimiters));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(context.getSpring().getBean(WTFSocketTCPHandler.class));
    }
}
