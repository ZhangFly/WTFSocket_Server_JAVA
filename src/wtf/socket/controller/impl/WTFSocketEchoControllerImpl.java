package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketSimpleController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.workflow.response.WTFSocketResponse;

import java.util.List;

/**
 * 回声控制器
 * <p>
 * Created by ZFly on 2017/4/29.
 */
public enum WTFSocketEchoControllerImpl implements WTFSocketSimpleController {

    INSTANCE;

    @Override
    public boolean isResponse(WTFSocketMessage msg) {
        return true;
    }

    @Override
    public boolean work(WTFSocketClient source, WTFSocketMessage request, WTFSocketResponse responses) throws WTFSocketException {
        final WTFSocketMessage echo = request.makeResponse();
        echo.setBody(request.getBody());
        responses.addMessage(echo);
        return true;
    }
}
