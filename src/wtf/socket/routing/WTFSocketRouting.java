package wtf.socket.routing;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.routing.item.WTFSocketRoutingDebugItem;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

/**
 * 路由
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
@Scope("prototype")
public class WTFSocketRouting {

    private WTFSocketServer context;

    private final WTFSocketRoutingItemMap<WTFSocketRoutingTmpItem> tmpMap = new WTFSocketRoutingItemMap<>();

    private final WTFSocketRoutingItemMap<WTFSocketRoutingFormalItem> formalMap = new WTFSocketRoutingItemMap<>();

    private final WTFSocketRoutingItemMap<WTFSocketRoutingDebugItem> debugMap = new WTFSocketRoutingItemMap<>();

    public WTFSocketRouting() {
        formalMap.add(new WTFSocketRoutingFormalItem(new WTFSocketRoutingTmpItem(context, new WTFSocketDefaultIOTerm())) {{
            setAddress("server");
            setCover(false);
            addAuthTarget("*");
        }});
        formalMap.add(new WTFSocketRoutingFormalItem(new WTFSocketRoutingTmpItem(context, new WTFSocketDefaultIOTerm())) {{
            setAddress("heartbeat");
            setCover(false);
            addAuthTarget("*");
        }});
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

    public void setContext(WTFSocketServer context) {
        this.context = context;
    }

//    public void setContext(WTFSocketServer context) {
//        this.context = context;
//    }
}
