package wtf.socket.routing.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;

import javax.annotation.Resource;

/**
 * 路由表对象
 * 每条对象对应一个实际的目标
 * <p>
 * Created by ZFly on 2017/4/23.
 */
@Component
@Scope("prototype")
public abstract class WTFSocketClient {

    protected final Log logger = LogFactory.getLog(this.getClass());

    WTFSocketClient() {}

    /**
     * 自身通讯地址
     */
    private String address;
    /**
     * 终端对象
     */
    private WTFSocketIOTerm term;
    /**
     * 接受的协议类型
     */
    private String accept;
    /**
     * 终端类型（iOS, Android, Hardware）
     */
    private String deviceType = "Unknown";
    /**
     * 是否允许覆盖
     */
    private boolean cover = true;
    /**
     * 允许用户为客户端添加自定义Tag信息
     */
    private Object tag;

    @Resource
    private WTFSocketServer context;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? "Unknown" : deviceType;
    }

    public WTFSocketIOTerm getTerm() {
        return term;
    }

    public void setTerm(WTFSocketIOTerm term) {
        this.term = term;
    }

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    public WTFSocketServer getContext() {
        return context;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void close() {
        try {
            getContext().getEventGroup().publishEvent(this, null, WTFSocketEventsType.Disconnect);
        } catch (WTFSocketException e) {
            logger.error(e.getMessage());
        } finally {
            getTerm().close();
        }
    }
}
