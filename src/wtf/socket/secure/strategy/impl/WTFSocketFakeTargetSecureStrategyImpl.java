package wtf.socket.secure.strategy.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 限制了Debug客户端只能向服务器发送消息
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketFakeTargetSecureStrategyImpl extends WTFSocketBaseSecureStrategyImpl {

    @Override
    public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        if (context.getRouting().getDebugMap().contains(msg.getFrom()) && !StringUtils.equals(msg.getTo(), "server"))
            throw new WTFSocketInvalidTargetException(msg.getTo()).setOriginalMsg(msg);

        doNext(context, msg);
    }
}
