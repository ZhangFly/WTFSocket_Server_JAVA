package wtf.socket.exception.fatal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocket;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;

/**
 * 致命异常
 * 一般指协议无法被解析
 * 不可回溯发送源的异常
 *
 * Created by zfly on 2017/4/22.
 */
public abstract class WTFSocketFatalException extends WTFSocketException {

    private static final Log logger = LogFactory.getLog(WTFSocketFatalException.class);

    public WTFSocketFatalException(String msg) {
        super(msg);
        logger.error(getClass().getSimpleName() + ": " + getMessage());
        WTFSocket.EVENTS_GROUP.notifyEventsListener(null, this, WTFSocketEventsType.FatalException);
    }
}
