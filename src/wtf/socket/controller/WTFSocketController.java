package wtf.socket.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.WTFSocketPriority;
import wtf.socket.workflow.request.WTFSocketRequest;
import wtf.socket.workflow.response.WTFSocketResponse;


/**
 * 控制器接口
 *
 * Created by ZFly on 2017/5/3.
 */
public interface WTFSocketController {

    Log logger = LogFactory.getLog(WTFSocketController.class);

    /**
     * 控制器优先级
     * 数值越低优先级越高，默认的优先级为 WTFSocketPriority.MEDIUM
     *
     * @return 优先级数值
     */
    default int priority() {
        return WTFSocketPriority.MEDIUM;
    }

    /**
     * 是否响应该消息
     *
     * @param msg 消息对象
     *
     * @return 是否响应
     */
    boolean isResponse(WTFSocketMessage msg);

    boolean work(WTFSocketRequest request, WTFSocketResponse response) throws WTFSocketException;
}
