package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;
import wtf.socket.util.WTFSocketPriority;

import java.util.List;

/**
 * 无条件注册控制器
 * <p>
 * Created by zfly on 2017/4/29.
 */
public enum WTFSocketUnconditionalRegisterControllerImpl implements WTFSocketController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGHEST;
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return true;
    }

    @Override
    public boolean work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        if (source instanceof WTFSocketRoutingTmpItem) {
            source.setAddress(request.getFrom());
            source.setAccept(request.getVersion());
            source.setDeviceType("Unknown");
            ((WTFSocketRoutingTmpItem) source).shiftToFormal();
        }
        return false;
    }
}
