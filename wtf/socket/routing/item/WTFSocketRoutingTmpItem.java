package wtf.socket.routing.item;

import wtf.socket.io.term.WTFSocketTerm;

import java.util.concurrent.TimeUnit;

/**
 * 临时对象
 *
 * Created by zfly on 2017/4/23.
 */
public class WTFSocketRoutingTmpItem extends WTFSocketRoutingItem {

    /**
     * 过期时间
     */
    private long expires = 0;

    public WTFSocketRoutingTmpItem(WTFSocketTerm term) {
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
}
