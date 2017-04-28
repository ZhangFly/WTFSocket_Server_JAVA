package wtf.socket.secure.strategy;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.containsTarget")
public class WTFSocketContainsTargetStrategy implements WTFSocketSecureStrategy {

    @Override
    public void invoke(WTFSocketMsg msg) throws WTFSocketInvalidTargetException{
        if (!WTFSocket.ROUTING.FORMAL_MAP.contains(msg.getTo()))
            throw (WTFSocketInvalidTargetException) new WTFSocketInvalidTargetException(msg.getTo()).setOriginalMsg(msg);
    }
}
