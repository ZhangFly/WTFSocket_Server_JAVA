package wtf.socket.controller.impl;

import wtf.socket.controller.WTFSocketSimpleController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.routing.client.WTFSocketTmpClient;
import wtf.socket.WTFSocketPriority;
import wtf.socket.workflow.response.WTFSocketResponse;

/**
 * 无条件注册控制器
 * <p>
 * Created by zfly on 2017/4/29.
 */
public enum WTFSocketUnconditionalRegisterControllerImpl implements WTFSocketSimpleController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGHEST;
    }

    @Override
    public boolean isResponse(WTFSocketMessage msg) {
        return true;
    }

    @Override
    public boolean work(WTFSocketClient source, WTFSocketMessage request, WTFSocketResponse responses) throws WTFSocketException {
        if (source instanceof WTFSocketTmpClient) {
            source.setAddress(request.getFrom());
            source.setAccept(request.getVersion());
            source.setDeviceType("Unknown");
            ((WTFSocketTmpClient) source).shiftToFormal();
        }
        return false;
    }
}
