package wtf.socket.routing.item;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;

import java.util.concurrent.TimeUnit;

/**
 * 临时客户端
 * <p>
 * Created by ZFly on 2017/4/23.
 */
@Component
@Scope("prototype")
public class WTFSocketRoutingTmpItem extends WTFSocketRoutingItem {

    /**
     * 过期时间
     */
    private long expires = TimeUnit.MINUTES.toMillis(1);

    public void setExpires(int duration, TimeUnit unit) {
        expires = System.currentTimeMillis() + unit.toMillis(duration);
    }

    public boolean isExpires() {
        return expires < System.currentTimeMillis();
    }

    public void shiftToFormal() throws WTFSocketException{
        getContext().getRouting().getTmpMap().remove(this);
        final WTFSocketRoutingFormalItem newFormal = new WTFSocketRoutingFormalItem();
        BeanUtils.copyProperties(this, newFormal);
        getContext().getRouting().getFormalMap().add(newFormal);
    }

    public void shiftToDebug() throws WTFSocketException {
        getContext().getRouting().getTmpMap().remove(this);
        final WTFSocketRoutingDebugItem newDebug = new WTFSocketRoutingDebugItem();
        BeanUtils.copyProperties(this, newDebug);
        getContext().getRouting().getDebugMap().add(newDebug);
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
