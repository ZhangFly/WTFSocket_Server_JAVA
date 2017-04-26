package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.fakeSource")
public class WTFSocketFakeSourceStrategy implements WTFSocketSecureStrategy {

    @Override
    public void invoke(WTFSocketMsg msg) throws WTFSocketInvalidSourceException {
        final WTFSocketRoutingFormalItem source = WTFSocket.ROUTING.FORMAL_MAP.getItem(msg.getFrom());
        if (source == null || !StringUtils.equals(source.getTerm().getIoTag(), msg.getIoTag()))
            throw new WTFSocketInvalidSourceException(msg.getFrom());
    }
}
