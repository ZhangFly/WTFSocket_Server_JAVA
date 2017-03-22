package wtf.socket.registry.items;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

public abstract class WTFSocketRegistryItem {

    protected Long timeout = Long.MAX_VALUE;
    protected final Channel channel;
    protected final WTFSocketConnectType connectType;
    protected final String name;

    protected WTFSocketRegistryItem(final String name, final Channel channel, final WTFSocketConnectType connectType) {
        this.name = name;
        this.channel = channel;
        this.connectType = connectType;
    }

    public Channel getChannel() {
        return channel;
    }

    public WTFSocketConnectType getConnectType() {
        return connectType;
    }

    public String getName() {
        return name;
    }

    public void expire(final int seconds) {
        timeout = System.currentTimeMillis() + seconds * 1_000;
    }

    public boolean isExpire() {
        return timeout < System.currentTimeMillis();
    }
}
