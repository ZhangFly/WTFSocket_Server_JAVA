package wtf.socket.io.term;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang.StringUtils;

/**
 * 终端默认实现类
 * 使用 Netty 实现数据发送
 *
 * Created by ZFly on 2017/4/22.
 */
public class WTFSocketDefaultIOTerm extends WTFSocketBaseIOTerm {

    private Channel channel;

    @Override
    public void write(String data) {
        if (channel != null && channel.isWritable()) {
            if (StringUtils.equals("TCP", connectType)) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(data.getBytes());
                channel.writeAndFlush(byteBuf);
            }
            if (StringUtils.equals("WebSocket", connectType)) {
                channel.writeAndFlush(new TextWebSocketFrame(data));
            }
        }
    }

    @Override
    public void close() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
