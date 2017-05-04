package wtf.socket.io.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;

import javax.annotation.Resource;

/**
 * Netty WebSocket初始化函数
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public class WTFSocketWebSocketInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private WTFSocketServer context;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec-http", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("handler", context.getSpring().getBean(WTFSocketWebSocketHandler.class));
    }
}
