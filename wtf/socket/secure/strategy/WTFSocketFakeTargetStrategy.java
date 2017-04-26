package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.exception.normal.WTFSocketInvalidTargetException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.fakeTarget")
public class WTFSocketFakeTargetStrategy implements WTFSocketSecureStrategy {

    @Override
    public void invoke(WTFSocketMsg msg) throws WTFSocketInvalidTargetException {
        if (StringUtils.startsWith(msg.getFrom(), "Debug") && !StringUtils.equals(msg.getTo(), "server"))
            throw (WTFSocketInvalidTargetException) new WTFSocketInvalidTargetException(msg.getTo()).setOriginalMsg(msg);
    }
}
