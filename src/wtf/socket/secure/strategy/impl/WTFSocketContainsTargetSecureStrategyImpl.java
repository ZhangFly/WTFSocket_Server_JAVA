package wtf.socket.secure.strategy.impl;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 发送目标是否已注册为正式客户端
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketContainsTargetSecureStrategyImpl extends WTFSocketBaseSecureStrategyImpl {

    @Override
    public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        if (!context.getRouting().getFormalMap().contains(msg.getTo()) && !context.getRouting().getDebugMap().contains(msg.getTo()))
            throw new WTFSocketInvalidTargetException("Target [" + msg.getTo() + "] was never registered").setOriginalMsg(msg);
        doNext(context, msg);
    }
}
