package wtf.socket.routing;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.io.WTFSocketIOTerm;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.routing.client.WTFSocketDebugClient;
import wtf.socket.routing.client.WTFSocketFormalClient;
import wtf.socket.routing.client.WTFSocketTmpClient;

import javax.annotation.Resource;

/**
 * 路由
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@Component
public class WTFSocketRouting {

    @Resource
    private WTFSocketServer context;

    private final WTFSocketRoutingItemMap<WTFSocketTmpClient> tmpMap = new WTFSocketRoutingItemMap<>();

    private final WTFSocketRoutingItemMap<WTFSocketFormalClient> formalMap = new WTFSocketRoutingItemMap<>();

    private final WTFSocketRoutingItemMap<WTFSocketDebugClient> debugMap = new WTFSocketRoutingItemMap<>();

    public WTFSocketRouting() {
    }

    /**
     * 新注册的终端只能被加入临时表
     *
     * @param term 连接终端
     */
    public void register(WTFSocketIOTerm term) {
        final WTFSocketTmpClient item = context.getSpring().getBean(WTFSocketTmpClient.class);
        item.setTerm(term);
        item.open();
    }

    public void unRegister(WTFSocketIOTerm term) {
        for (WTFSocketRoutingItemMap map : values()) {
            if (map.contains(term.getIoTag())) {
                map.getItem(term.getIoTag()).close();
            }
        }
    }

    public WTFSocketRoutingItemMap<WTFSocketTmpClient> getTmpMap() {
        return tmpMap;
    }

    public WTFSocketRoutingItemMap<WTFSocketFormalClient> getFormalMap() {
        return formalMap;
    }

    public WTFSocketRoutingItemMap<WTFSocketDebugClient> getDebugMap() {
        return debugMap;
    }

    public WTFSocketClient getItem(String key) {
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
