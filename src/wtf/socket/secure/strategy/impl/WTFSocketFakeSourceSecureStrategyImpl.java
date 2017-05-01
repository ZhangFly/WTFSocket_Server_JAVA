package wtf.socket.secure.strategy.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;

/**
 * 发送源是通讯地址和连接io地址是否匹配
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketFakeSourceSecureStrategyImpl extends WTFSocketBaseSecureStrategyImpl {

    @Override
    public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        final WTFSocketRoutingFormalItem source = context.getRouting().getFormalMap().getItem(msg.getFrom());
        if (source == null)
            throw new WTFSocketInvalidSourceException(msg.getFrom());

        if (!StringUtils.equals(source.getAddress(), "heartbeat") && !StringUtils.equals(source.getTerm().getIoTag(), msg.getIoTag()))
            throw new WTFSocketInvalidSourceException(msg.getFrom());

        doNext(context, msg);
    }
}
