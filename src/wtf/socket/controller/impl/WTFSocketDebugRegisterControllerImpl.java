package wtf.socket.controller.impl;

import org.apache.commons.lang.StringUtils;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;
import wtf.socket.util.WTFSocketPriority;

import java.util.List;

/**
 * 调试客户端注册控制器
 * <p>
 * Created by ZFly on 2017/4/29.
 */
public enum WTFSocketDebugRegisterControllerImpl implements WTFSocketController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGHEST;
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return StringUtils.startsWith(msg.getFrom(), "Debug_");
    }

    @Override
    public boolean work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        if (source != null && source instanceof WTFSocketRoutingTmpItem) {
            source.setAddress(request.getFrom());
            source.setAccept(request.getVersion());
            source.setDeviceType("Debug");
            ((WTFSocketRoutingTmpItem) source).shiftToDebug();
        }
        return false;
    }
}
