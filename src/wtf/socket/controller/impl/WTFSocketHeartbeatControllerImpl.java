package wtf.socket.controller.impl;

import org.apache.commons.lang.StringUtils;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.util.WTFSocketPriority;

import java.util.List;

/**
 *
 * Created by ZFly on 2017/5/1.
 */
public enum  WTFSocketHeartbeatControllerImpl implements WTFSocketController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGH;
    }

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return StringUtils.equals("heartbeat", msg.getTo());
    }

    @Override
    public boolean work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        final WTFSocketMsg response = request.makeResponse();
        response.setBody(request.getBody());
        responses.add(response);
        return true;
    }
}
