package wtf.socket.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class WTFSocketTCPInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
        pipeline.addLast(new DelimiterBasedFrameDecoder(65536, delimiter));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new WTFSocketTCPHandler());
    }
}
