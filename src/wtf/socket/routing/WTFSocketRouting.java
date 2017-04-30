package wtf.socket.routing;

import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;
import wtf.socket.routing.item.WTFSocketRoutingDebugItem;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

/**
 * 路由
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public class WTFSocketRouting {

    private final WTFSocketServer context;

    private final WTFSocketRoutingItemMap<WTFSocketRoutingTmpItem> tmpMap = new WTFSocketRoutingItemMap<>();

    private final WTFSocketRoutingItemMap<WTFSocketRoutingFormalItem> formalMap = new WTFSocketRoutingItemMap<>();

    private final WTFSocketRoutingItemMap<WTFSocketRoutingDebugItem> debugMap = new WTFSocketRoutingItemMap<>();

    public WTFSocketRouting(WTFSocketServer context) {
        this.context = context;
    }

    /**
     * 新注册的终端只能被加入临时表
     *
     * @param term 连接终端
     */
    public void register(WTFSocketIOTerm term) throws WTFSocketException {
        final WTFSocketRoutingTmpItem item = new WTFSocketRoutingTmpItem(context, term);
        item.open();
    }

    public void unRegister(WTFSocketIOTerm term) throws WTFSocketException {
        for (WTFSocketRoutingItemMap map : values()) {
            if (map.contains(term.getIoTag())) {
                map.getItem(term.getIoTag()).close();
            }
        }
    }

    public WTFSocketRoutingItemMap<WTFSocketRoutingTmpItem> getTmpMap() {
        return tmpMap;
    }

    public WTFSocketRoutingItemMap<WTFSocketRoutingFormalItem> getFormalMap() {
        return formalMap;
    }

    public WTFSocketRoutingItemMap<WTFSocketRoutingDebugItem> getDebugMap() {
        return debugMap;
    }

    public WTFSocketRoutingItem getItem(String key) {
        if (formalMap.contains(key))
            return formalMap.getItem(key);
        if (debugMap.contains(key))
            return debugMap.getItem(key);
        return tmpMap.getItem(key);
    }

    public boolean contains(String key) {
        return formalMap.contains(key) || debugMap.contains(key) || tmpMap.contains(key);
    }


    public WTFSocketRoutingItemMap[] values() {
        return new WTFSocketRoutingItemMap[]{tmpMap, formalMap, debugMap};
    }
}
