package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketKeepWordsException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.sourceKeepWords")
public class WTFSocketSourceKeepWordsStrategy implements WTFSocketSecureStrategy {

    @Override
    public void invoke(WTFSocketMsg msg) throws WTFSocketKeepWordsException {
        if (StringUtils.equals("server", msg.getFrom()))
            throw new WTFSocketKeepWordsException(msg.getFrom());
    }
}
