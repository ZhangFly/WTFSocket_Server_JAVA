package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 * 限制了Debug客户端只能向服务器发送消息
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.fakeTarget")
public class WTFSocketFakeTargetStrategy implements WTFSocketSecureStrategy {

    @Override
    public void execute(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketInvalidTargetException {
        if (context.getRouting().getDebugMap().contains(msg.getFrom()) && !StringUtils.equals(msg.getTo(), "server"))
            throw (WTFSocketInvalidTargetException) new WTFSocketInvalidTargetException(msg.getTo()).setOriginalMsg(msg);
    }
}
