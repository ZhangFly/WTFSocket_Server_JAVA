package wtf.socket.registry.items;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

public class WTFSocketRegistryTmpItem extends WTFSocketRegistryItem {

    public WTFSocketRegistryTmpItem(final String name, final Channel channel, final WTFSocketConnectType connectType) {
        super(name, channel, connectType);
    }

}
