package wtf.socket.event;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 监听事件组
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
@Scope("prototype")
public class WTFSocketEventListenersGroup {

    private final Map<WTFSocketEventsType, Set<WTFSocketEventListener>> group = new HashMap<WTFSocketEventsType, Set<WTFSocketEventListener>>(WTFSocketEventsType.values().length) {{
        for (WTFSocketEventsType eventsType : WTFSocketEventsType.values()) {
            put(eventsType, new HashSet<>(1));
        }
    }};

    /**
     * 添加事件监听者
     *
     * @param eventListener 监听者
     * @param eventsType    事件类型
     */
    public void addEventListener(WTFSocketEventListener eventListener, WTFSocketEventsType eventsType) {
        group.get(eventsType).add(eventListener);
    }

    /**
     * 移除事件监听者
     *
     * @param eventListener 监听者
     * @param eventsType    事件类型
     */
    public void removeEventListener(WTFSocketEventListener eventListener, WTFSocketEventsType eventsType) {
        group.get(eventsType).remove(eventListener);
    }

    /**
     * 发生某类型事件
     *
     * @param item       发送源客户端
     * @param info       附加消息
     * @param eventsType 事件类型
     *
     * @throws WTFSocketException 异常信息
     */
    public void publishEvent(WTFSocketRoutingItem item, Object info, WTFSocketEventsType eventsType) throws WTFSocketException {
        for (WTFSocketEventListener eventListener : group.get(eventsType)) {
            eventListener.eventOccurred(item, info);
        }
    }

}
