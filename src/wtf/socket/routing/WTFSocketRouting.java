package wtf.socket.routing;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingDebugItem;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

import java.util.Arrays;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@Component("wtf.socket.routing")
public class WTFSocketRouting {

    public final WTFSocketRoutingItemMap<WTFSocketRoutingTmpItem> TMP_MAP = new WTFSocketRoutingItemMap<>();

    public final WTFSocketRoutingItemMap<WTFSocketRoutingFormalItem> FORMAL_MAP = new WTFSocketRoutingItemMap<WTFSocketRoutingFormalItem>() {{
        add(new WTFSocketRoutingFormalItem(new WTFSocketDefaultIOTerm()) {{
            // 默认添加 server 对象
            // server 对象代表服务器，不可被覆盖
            setCover(false);
            setAddress("server");
        }});
    }};

    public final WTFSocketRoutingItemMap<WTFSocketRoutingDebugItem> DEBUG_MAP = new WTFSocketRoutingItemMap<>();

    /**
     * 新注册的终端只能被加入临时表
     *
     * @param term 连接终端
     */
    public void register(WTFSocketIOTerm term) throws WTFSocketException{
        final WTFSocketRoutingTmpItem item = new WTFSocketRoutingTmpItem(term);
        item.login();
    }

    public void unRegister(WTFSocketIOTerm term) throws WTFSocketException {
        for (WTFSocketRoutingItemMap map : values()) {
            if (map.contains(term.getIoTag())) {
                map.getItem(term.getIoTag()).logout();
            }
        }
    }

    public WTFSocketRoutingItem getItem(String key) {
        if (FORMAL_MAP.contains(key))
            return FORMAL_MAP.getItem(key);
        if (DEBUG_MAP.contains(key))
            return DEBUG_MAP.getItem(key);
        return TMP_MAP.getItem(key);
    }

    public boolean contains(String key) {
        return FORMAL_MAP.contains(key) || DEBUG_MAP.contains(key) || TMP_MAP.contains(key);
    }


    public WTFSocketRoutingItemMap[] values() {
        return new WTFSocketRoutingItemMap[] {TMP_MAP, FORMAL_MAP, DEBUG_MAP};
    }
}
