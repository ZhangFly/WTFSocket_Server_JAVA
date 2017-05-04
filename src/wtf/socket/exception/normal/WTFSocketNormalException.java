package wtf.socket.exception.normal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;

/**
 * 普通异常
 * 一般指协议已被正常解析
 * 可回溯发送源的异常
 *
 * Created by ZFly on 2017/4/22.
 */
public abstract class WTFSocketNormalException extends WTFSocketException {

    private static final Log logger = LogFactory.getLog(WTFSocketNormalException.class);

    public WTFSocketNormalException(String msg) {
        super(msg);
        logger.warn(getMessage());
    }

}
