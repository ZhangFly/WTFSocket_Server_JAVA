package wtf.socket.registry;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

/**
 * 用户注册表条目
 */
public class WTFSocketUserRegistryItem {

    private String name;
    private Channel cxt;
    private String accept;
    private WTFSocketConnectType connectType;
    private String deviceType;

    WTFSocketUserRegistryItem(String name, Channel channel, String accept, WTFSocketConnectType connectType, String deviceType) {
        this.name = name;
        this.cxt = channel;
        this.accept = accept;
        this.connectType = connectType;
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public Channel getCxt() {
        return cxt;
    }

    public String getAccept() {
        return accept;
    }

    public WTFSocketConnectType getConnectType() {
        return connectType;
    }

    public String getDeviceType() {
        return deviceType;
    }

}
