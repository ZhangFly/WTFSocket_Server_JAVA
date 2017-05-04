package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketSimpleController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.WTFSocketPriority;
import wtf.socket.workflow.response.WTFSocketResponse;

/**
 * 消息转发控制器
 * <p>
 * Created by zfly on 2017/4/29.
 */
public enum WTFSocketMsgForwardingControllerImpl implements WTFSocketSimpleController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.LOWEST;
    }

    @Override
    public boolean isResponse(WTFSocketMessage msg) {
        return true;
    }

    @Override
    public boolean work(WTFSocketClient source, WTFSocketMessage request, WTFSocketResponse responses) throws WTFSocketException {
        if (responses.isEmpty()) {
            responses.addMessage(request);
        }
        return true;
    }
}
