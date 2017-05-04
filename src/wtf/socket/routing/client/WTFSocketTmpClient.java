package wtf.socket.routing.client;

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
public class WTFSocketTmpClient extends WTFSocketClient {

    /**
     * 过期时间
     */
    private long expires = TimeUnit.MINUTES.toMillis(1);

    WTFSocketTmpClient() {}

    public void setExpires(int duration, TimeUnit unit) {
        expires = System.currentTimeMillis() + unit.toMillis(duration);
    }

    public boolean isExpires() {
        return expires < System.currentTimeMillis();
    }

    public void shiftToFormal() throws WTFSocketException{
        final WTFSocketFormalClient newFormal = (WTFSocketFormalClient) getContext().getSpring().getBean("WTFSocketFormalClient");
        BeanUtils.copyProperties(this, newFormal);
        getContext().getRouting().getFormalMap().add(newFormal);
        getContext().getRouting().getTmpMap().remove(this);
    }

    public void shiftToDebug() throws WTFSocketException {
        final WTFSocketDebugClient newDebug = getContext().getSpring().getBean(WTFSocketDebugClient.class);
        BeanUtils.copyProperties(this, newDebug);
        getContext().getRouting().getDebugMap().add(newDebug);
        getContext().getRouting().getTmpMap().remove(this);
    }

    public void open() {
        try {
            getContext().getRouting().getTmpMap().add(this);
            getContext().getEventGroup().publishEvent(this, null, WTFSocketEventsType.Connect);
        } catch (WTFSocketException e) {
            logger.error(e.getMessage());
            this.close();
        }
    }

    public void close()  {
        super.close();
        getContext().getRouting().getTmpMap().remove(this);
    }
}
