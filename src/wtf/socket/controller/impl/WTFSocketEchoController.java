package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.List;

/**
 * 回声控制器
 * <p>
 * Created by ZFly on 2017/4/29.
 */
public enum WTFSocketEchoController implements WTFSocketController {

    INSTANCE;

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        return true;
    }

    @Override
    public boolean work(WTFSocketRoutingItem source, WTFSocketMsg request, List<WTFSocketMsg> responses) throws WTFSocketException {
        if (request != null) {
            final WTFSocketMsg echo = request.makeResponse();
            echo.setBody(request.getBody());
            responses.add(echo);
        }
        return true;
    }
}
