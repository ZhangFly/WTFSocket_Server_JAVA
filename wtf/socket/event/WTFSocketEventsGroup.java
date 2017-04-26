package wtf.socket.event;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.eventsGroup")
public class WTFSocketEventsGroup {

    private final Map<WTFSocketEventsType, Set<WTFSocketEventListener>> group = new HashMap<WTFSocketEventsType, Set<WTFSocketEventListener>>(4) {{
        for (WTFSocketEventsType eventsType : WTFSocketEventsType.values()) {
            put(eventsType, new HashSet<>(3));
        }
    }};

    public void addEventListener(WTFSocketEventListener eventListener, WTFSocketEventsType eventsType) {
        group.get(eventsType).add(eventListener);
    }

    public void removeEventListener(WTFSocketEventListener eventListener, WTFSocketEventsType eventsType) {
        group.get(eventsType).remove(eventListener);
    }

    public void notifyEventsListener(WTFSocketRoutingItem item, Object info, WTFSocketEventsType eventsType) {
        group.get(eventsType).forEach(eventListener -> eventListener.notify(item, info));
    }

}
