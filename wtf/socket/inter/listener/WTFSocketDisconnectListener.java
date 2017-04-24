package wtf.socket.inter.listener;

import wtf.socket.routing.item.WTFSocketRoutingItem;

/**
 * 断开连接监听
 *
 * Created by zfly on 2017/4/24.
 */
@FunctionalInterface
public interface WTFSocketDisconnectListener {
    void invoke(WTFSocketRoutingItem item);
}
