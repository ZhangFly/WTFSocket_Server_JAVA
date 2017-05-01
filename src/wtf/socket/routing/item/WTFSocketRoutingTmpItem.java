package wtf.socket.routing.item;

import wtf.socket.WTFSocketServer;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;

import java.util.concurrent.TimeUnit;

/**
 * 临时客户端
 * <p>
 * Created by ZFly on 2017/4/23.
 */
public class WTFSocketRoutingTmpItem extends WTFSocketRoutingItem {

    /**
     * 过期时间
     */
    private long expires = 0;

    public WTFSocketRoutingTmpItem(WTFSocketServer context, WTFSocketIOTerm term) {
        super(context, term);
        setExpires(1, TimeUnit.MINUTES);
    }

    public void setExpires(int duration, TimeUnit unit) {
        expires = System.currentTimeMillis() + unit.toMillis(duration);
    }

    public boolean isExpires() {
        return expires < System.currentTimeMillis();
    }

    public void shiftToFormal() {
        getContext().getRouting().getTmpMap().remove(this);
        getContext().getRouting().getFormalMap().add(new WTFSocketRoutingFormalItem(this));
    }

    public void shiftToDebug() {
        getContext().getRouting().getTmpMap().remove(this);
        getContext().getRouting().getDebugMap().add(new WTFSocketRoutingDebugItem(this));
    }

    public void open() throws WTFSocketException {
        getContext().getRouting().getTmpMap().add(this);
        getContext().getEventsGroup().publishEvent(this, null, WTFSocketEventsType.Connect);
    }

    public void close() throws WTFSocketException {
        super.close();
        getContext().getRouting().getTmpMap().remove(this);
    }
}
