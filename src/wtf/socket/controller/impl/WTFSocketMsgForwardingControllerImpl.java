package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.util.WTFSocketPriority;

import java.util.List;

/**
 * 消息转发控制器
 * <p>
 * Created by zfly on 2017/4/29.
 */
public enum WTFSocketMsgForwardingControllerImpl implements WTFSocketController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.LOWEST;
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return true;
    }

    @Override
    public boolean work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        if (responses.isEmpty()) {
            responses.add(request);
        }
        return true;
    }
}
