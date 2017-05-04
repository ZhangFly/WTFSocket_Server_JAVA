package wtf.socket.event;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.routing.client.WTFSocketClient;

/**
 * 事件监听者接口
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@FunctionalInterface
public interface WTFSocketEventListener {
    void eventOccurred(WTFSocketClient item, Object info) throws WTFSocketException;
}
