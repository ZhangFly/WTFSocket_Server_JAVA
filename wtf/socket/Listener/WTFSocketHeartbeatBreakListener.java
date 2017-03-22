package wtf.socket.Listener;

import wtf.socket.registry.items.WTFSocketRegistryItem;

@FunctionalInterface
public interface WTFSocketHeartbeatBreakListener {

    void heartbeatBreak(WTFSocketRegistryItem item);

}
