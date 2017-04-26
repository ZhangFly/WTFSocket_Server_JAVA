package wtf.socket.routing.item;

import wtf.socket.WTFSocket;
import wtf.socket.io.WTFSocketIOTerm;

import java.util.concurrent.TimeUnit;

/**
 * 临时对象
 * Created by zfly on 2017/4/23.
 */
public class WTFSocketRoutingTmpItem extends WTFSocketRoutingItem {

    /**
     * 过期时间
     */
    private long expires = 0;

    public WTFSocketRoutingTmpItem(WTFSocketIOTerm term) {
        super(term);
        setExpires(1, TimeUnit.MINUTES);
    }

    public WTFSocketRoutingTmpItem(WTFSocketRoutingItem item) {
        super(item);
        setExpires(1, TimeUnit.MINUTES);
    }

    public void setExpires(int duration, TimeUnit unit) {
        expires = System.currentTimeMillis() + unit.toMillis(duration);
    }

    public boolean isExpires() {
        return expires < System.currentTimeMillis();
    }

    public void shiftToFormal() {
        WTFSocket.ROUTING.TMP_MAP.remove(this);
        WTFSocket.ROUTING.FORMAL_MAP.add(new WTFSocketRoutingFormalItem(this));
    }

    public void shiftToDebug() {
        WTFSocket.ROUTING.TMP_MAP.remove(this);
        WTFSocket.ROUTING.DEBUG_MAP.add(new WTFSocketRoutingDebugItem(this));
    }

    public void logout() {
        super.logout();
        WTFSocket.ROUTING.TMP_MAP.remove(this);
    }
}
