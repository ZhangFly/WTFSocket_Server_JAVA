package wtf.socket.secure.strategy;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.containsSource")
public class WTFSocketContainsSourceStrategy implements WTFSocketSecureStrategy {

    @Override
    public void invoke(WTFSocketMsg msg) throws WTFSocketInvalidSourceException {
        if (!WTFSocket.ROUTING.FORMAL_MAP.contains(msg.getFrom()))
            throw new WTFSocketInvalidSourceException(msg.getFrom());
    }
}
