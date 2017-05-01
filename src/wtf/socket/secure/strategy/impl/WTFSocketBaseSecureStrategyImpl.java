package wtf.socket.secure.strategy.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.secure.strategy.WTFSocketSecureStrategy;

/**
 *
 * Created by ZFly on 2017/5/1.
 */
public abstract class WTFSocketBaseSecureStrategyImpl implements WTFSocketSecureStrategy{

    protected Log logger = LogFactory.getLog(this.getClass());

    protected WTFSocketSecureStrategy next;

    protected void doNext(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException {
        if (null != next) {
            next.check(context, msg);
        }
    }

    public void setNext(WTFSocketSecureStrategy next) {
        this.next = next;
    }
}
