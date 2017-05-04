package wtf.socket.controller.impl;

import org.apache.commons.lang.StringUtils;
import wtf.socket.controller.WTFSocketSimpleController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.routing.client.WTFSocketTmpClient;
import wtf.socket.WTFSocketPriority;
import wtf.socket.workflow.response.WTFSocketResponse;

/**
 * 调试客户端注册控制器
 * <p>
 * Created by ZFly on 2017/4/29.
 */
public enum WTFSocketDebugRegisterControllerImpl implements WTFSocketSimpleController {

    INSTANCE;

    @Override
    public int priority() {
        return WTFSocketPriority.HIGHEST;
    }

    @Override
    public boolean isResponse(WTFSocketMessage msg) {
        return StringUtils.startsWith(msg.getFrom(), "Debug_");
    }

    @Override
    public boolean work(WTFSocketClient source, WTFSocketMessage request, WTFSocketResponse responses) throws WTFSocketException {
        if (source != null && source instanceof WTFSocketTmpClient) {
            source.setAddress(request.getFrom());
            source.setAccept(request.getVersion());
            source.setDeviceType("Debug");
            ((WTFSocketTmpClient) source).shiftToDebug();
        }
        return false;
    }
}
