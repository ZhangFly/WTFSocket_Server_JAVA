package wtf.socket.secure.strategy.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketKeepWordsException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 是否使用了系统保留关键字
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public final class WTFSocketSourceKeepWordsSecureStrategyImpl extends WTFSocketBaseSecureStrategyImpl {

    @Override
    public void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        if (StringUtils.equals("server", msg.getFrom()))
            throw new WTFSocketKeepWordsException("Source can not be [server]");
        doNext(context, msg);
    }
}
