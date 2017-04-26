package wtf.socket.routing;

import org.springframework.stereotype.Component;
import wtf.socket.WTFSocket;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.io.WTFSocketIOTerm;
import wtf.socket.io.term.WTFSocketDefaultIOTerm;
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
    public void register(WTFSocketIOTerm term) {
        final WTFSocketRoutingTmpItem item = new WTFSocketRoutingTmpItem(term);
        WTFSocket.ROUTING.TMP_MAP.add(item);
        WTFSocket.EVENTS_GROUP.notifyEventsListener(item, null, WTFSocketEventsType.Connect);
    }

    public void unRegister(WTFSocketIOTerm term) {
        Arrays.stream(values())
                .filter(map -> map.contains(term.getIoTag()))
                .forEach(map -> {
                    map.getItem(term.getIoTag()).logout();
                });
    }

    public WTFSocketRoutingItemMap[] values() {
        return new WTFSocketRoutingItemMap[] {TMP_MAP, FORMAL_MAP, DEBUG_MAP};
    }
}
