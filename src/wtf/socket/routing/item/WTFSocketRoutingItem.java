package wtf.socket.routing.item;

import wtf.socket.WTFSocketServer;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;

/**
 * 路由表对象
 * 每条对象对应一个实际的目标
 * <p>
 * Created by ZFly on 2017/4/23.
 */
public abstract class WTFSocketRoutingItem{

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

    private WTFSocketServer context;

    public WTFSocketRoutingItem(WTFSocketServer context, WTFSocketIOTerm term) {
        this.context = context;
        this.term = term;
    }

    public WTFSocketRoutingItem(WTFSocketRoutingItem item) {
        if (item != null) {
            address = item.address;
            term = item.term;
            accept = item.accept;
            deviceType = item.deviceType;
            cover = item.cover;
            context = item.context;
        }
    }

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

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    public void close() throws WTFSocketException {
        getContext().getEventsGroup().publishEvent(this, null, WTFSocketEventsType.Disconnect);
        getTerm().close();
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
}
