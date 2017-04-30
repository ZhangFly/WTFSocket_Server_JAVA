package wtf.socket.secure.strategy;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 * 发送目标是否已注册为正式客户端
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.containsTarget")
public class WTFSocketContainsTargetStrategy implements WTFSocketSecureStrategy {

    @Override
    public void execute(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketInvalidTargetException {
        if (!context.getRouting().getFormalMap().contains(msg.getTo()))
            throw (WTFSocketInvalidTargetException) new WTFSocketInvalidTargetException(msg.getTo()).setOriginalMsg(msg);
    }
}
