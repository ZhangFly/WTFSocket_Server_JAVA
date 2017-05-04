package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketSimpleController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketFormalClient;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.WTFSocketPriority;
import wtf.socket.workflow.response.WTFSocketResponse;

/**
 *
 * Created by ZFly on 2017/5/1.
 */
public enum  WTFSocketHeartbeatControllerImpl implements WTFSocketSimpleController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGH;
    }

    @Override
    public boolean isResponse(WTFSocketMessage msg) {
        return msg.getMsgType() == 0;
    }

    @Override
    public boolean work(WTFSocketClient source, WTFSocketMessage request, WTFSocketResponse responses) throws WTFSocketException {
        final WTFSocketMessage response = request.makeResponse();
        response.setFrom("heartbeat");
        response.setBody(request.getBody());
        responses.addMessage(response);
        return true;
    }
}
