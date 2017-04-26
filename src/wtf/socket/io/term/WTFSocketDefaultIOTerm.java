package wtf.socket.io.term;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang.StringUtils;

/**
 * 终端默认实现类
 * 使用 netty 实现数据发送
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketDefaultIOTerm extends WTFSocketBaseIOTerm {

    private  static final String EOT = "\r\n";
    private Channel channel;

    @Override
    public void write(String data) {
        if (channel != null && channel.isWritable()) {
            if (StringUtils.equals("TCP", connectType)) {
                ByteBuf byteBuf = Unpooled.copiedBuffer((data + EOT).getBytes());
                channel.writeAndFlush(byteBuf);
            }
            if (StringUtils.equals("WebSocket", connectType)) {
                channel.writeAndFlush(new TextWebSocketFrame((data + EOT)));
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
