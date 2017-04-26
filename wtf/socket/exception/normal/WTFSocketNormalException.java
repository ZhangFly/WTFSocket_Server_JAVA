package wtf.socket.exception.normal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.WTFSocket;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;

/**
 * 普通异常
 * 一般指协议已被正常解析
 * 可回溯发送源的异常
 *
 * Created by zfly on 2017/4/22.
 */
public abstract class WTFSocketNormalException extends WTFSocketException {

    private static final Log logger = LogFactory.getLog(WTFSocketNormalException.class);

    /**
     * 造成异常的源数据
     */
    private WTFSocketMsg originalMsg;

    public WTFSocketNormalException(String msg) {
        super(msg);
    }

    public WTFSocketMsg getOriginalMsg() {
        return originalMsg;
    }

    public WTFSocketNormalException setOriginalMsg(WTFSocketMsg originalMsg) {
        this.originalMsg = originalMsg;
        final WTFSocketRoutingItem item = WTFSocket.ROUTING.FORMAL_MAP.getItem(originalMsg.getFrom());
        logger.warn(getClass().getSimpleName() + ":\n " + getMessage());
        WTFSocket.EVENTS_GROUP.notifyEventsListener(item, this, WTFSocketEventsType.NormalException);
        return this;
    }
}
