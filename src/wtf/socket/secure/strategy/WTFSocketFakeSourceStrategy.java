package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 * 发送源是通讯地址和连接io地址是否匹配
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.fakeSource")
public class WTFSocketFakeSourceStrategy implements WTFSocketSecureStrategy {

    @Override
    public void execute(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketInvalidSourceException {
        final WTFSocketRoutingFormalItem source = context.getRouting().getFormalMap().getItem(msg.getFrom());
        if (source == null)
            throw new WTFSocketInvalidSourceException(msg.getFrom());

        if (!StringUtils.equals(source.getAddress(), "heartbeat") && !StringUtils.equals(source.getTerm().getIoTag(), msg.getIoTag()))
            throw new WTFSocketInvalidSourceException(msg.getFrom());
    }
}
