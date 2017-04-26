package wtf.socket.routing.item;

import wtf.socket.WTFSocket;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.io.WTFSocketIOTerm;

/**
 * 路由表对象
 * 每条对象对应一个实际的目标
 *
 * Created by zfly on 2017/4/23.
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
    private String type;
    /**
     * 是否允许覆盖
     */
    private boolean cover = true;

    public WTFSocketRoutingItem(WTFSocketIOTerm term) {
        this.term = term;
    }

    public WTFSocketRoutingItem(WTFSocketRoutingItem item) {
        if (item != null) {
            address = item.address;
            term = item.term;
            accept = item.accept;
            type = item.type;
            cover = item.cover;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void logout() {
        WTFSocket.EVENTS_GROUP.notifyEventsListener(this, null, WTFSocketEventsType.Disconnect);
        getTerm().close();
    }
}
