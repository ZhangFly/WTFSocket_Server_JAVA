package wtf.socket.netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class WTFSocketTCPDecoder extends ByteToMessageDecoder {

    private StringBuffer buffer = new StringBuffer();
    private static final String  EOT = "\r\n";

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (byteBuf.readableBytes() < 0) {
            return;
        }

        byte[] bytes = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(bytes);

        buffer.append(new String(bytes));

        while (true) {
            int index = buffer.indexOf(EOT);
            if (index == -1) {
                break;
            }
            String packet = buffer.substring(0, index);
            buffer.delete(0, index + 2);
            list.add(packet);
        }

    }
}
