package wtf.socket.secure.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.fatal.WTFSocketKeepWordsException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.WTFSocketSecureStrategy;

/**
 * 是否使用了系统保留关键字
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component("wtf.socket.secure.strategy.sourceKeepWords")
public class WTFSocketSourceKeepWordsStrategy implements WTFSocketSecureStrategy {

    @Override
    public void execute(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketKeepWordsException {
        if (StringUtils.equals("server", msg.getFrom()) || StringUtils.equals("heartbeat", msg.getFrom()))
            throw new WTFSocketKeepWordsException(msg.getFrom());
    }
}
