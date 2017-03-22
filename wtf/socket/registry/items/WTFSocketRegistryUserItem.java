package wtf.socket.registry.items;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

/**
 * 用户注册表条目
 */
public class WTFSocketRegistryUserItem extends WTFSocketRegistryItem{

    private String accept;
    private String deviceType;

    public WTFSocketRegistryUserItem(final String name, final Channel channel, final WTFSocketConnectType connectType, final String accept, final String deviceType) {
        super(name, channel, connectType);
        this.accept = accept;
        this.deviceType = deviceType;
    }

    public String getAccept() {
        return accept;
    }

    public String getDeviceType() {
        return deviceType;
    }

}
