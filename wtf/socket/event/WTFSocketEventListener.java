package wtf.socket.event;

import wtf.socket.routing.item.WTFSocketRoutingItem;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@FunctionalInterface
public interface WTFSocketEventListener {
    void notify(WTFSocketRoutingItem item, Object info);
}
