package wtf.socket.secure.strategy.impl;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 发送源是否已注册为正式客户端
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketContainsSourceSecureStrategyImpl extends WTFSocketBaseSecureStrategyImpl {

    @Override
    public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        if (!context.getRouting().getFormalMap().contains(msg.getFrom()) && !context.getRouting().getDebugMap().contains(msg.getFrom()))
            throw new WTFSocketInvalidSourceException("Source [" + msg.getFrom() + "] was never registered");
        doNext(context, msg);
    }
}
