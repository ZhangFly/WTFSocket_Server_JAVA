package wtf.socket.registry;


import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;
import wtf.socket.registry.items.*;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static wtf.socket.registry.items.WTFSocketUserType.*;

/**
 * 用户注册表
 */
public class WTFSocketRegistry {

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private WTFSocketRegistry() {
    }

    public static void runExpire() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (WTFSocketUserType type : WTFSocketUserType.values()) {
                    for (WTFSocketRegistryItem item : WTFSocketMemCache.values(type)) {
                        if (item.isExpire()) {
                            WTFSocketRegistry.unRegister(item.getName());
                        }
                    }
                }
            }
        }, 210, 210, TimeUnit.SECONDS);
    }

    public static void register(final String name, final Channel channel, final WTFSocketConnectType connectType, final String version, final String deviceType) {

        if (isTmp(name)) {
            final WTFSocketRegistryItem item = new WTFSocketRegistryTmpItem(name, channel, connectType);
            item.expire(180);
            WTFSocketMemCache.set(TMP, name, item);
            return;
        }

        if (isDebug(name)) {
            if (WTFSocketMemCache.contains(TMP, "Tmp_" + channel.id())) {
                WTFSocketMemCache.del(TMP,  "Tmp_" + channel.id());
            }

            final WTFSocketRegistryItem item = new WTFSocketRegistryDebugItem(name, channel, connectType, version);
            item.expire(1_800);
            WTFSocketMemCache.set(DEBUG, name, item);
            return;
        }

        if (WTFSocketMemCache.contains(TMP, "Tmp_" + channel.id())) {
            WTFSocketMemCache.del(TMP,  "Tmp_" + channel.id());
        }

        final WTFSocketRegistryItem item = new WTFSocketRegistryUserItem(name, channel, connectType, version, deviceType);
        item.expire(180);
        WTFSocketMemCache.set(USER, name, item);
    }

    public static void unRegister(final String name) {

        if (isTmp(name)) {
            closeAndDelete(TMP, name);
            return;
        }

        if (isDebug(name)) {
            closeAndDelete(DEBUG, name);
            return;
        }

        closeAndDelete(USER, name);
    }

    public static boolean contains(final String name) {
        return isDebug(name) ? WTFSocketMemCache.contains(DEBUG, name) : WTFSocketMemCache.contains(USER, name);
    }


    public static WTFSocketRegistryItem get(final String name) {
        if (isTmp(name)) {
            return WTFSocketMemCache.get(TMP, name);
        }

        if (isDebug(name)) {
            return WTFSocketMemCache.get(DEBUG, name);
        }

        return WTFSocketMemCache.get(USER, name);
    }

    public static Collection<WTFSocketRegistryItem> values(final WTFSocketUserType type) {
        return WTFSocketMemCache.values(type);
    }

    public static void feed(final String name) {

        if (isDebug(name)) {
            WTFSocketMemCache.get(DEBUG, name).expire(1_800);
            return;
        }

        WTFSocketMemCache.get(USER, name).expire(180);
    }

    public static boolean isDebug(final String name) {
        return name.matches("^Debug_.*");
    }

    public static boolean isTmp(final String name) {
        return name.matches("^Tmp_.*");
    }

    private static void closeAndDelete(final WTFSocketUserType type, final String name) {
        if (!WTFSocketMemCache.contains(type, name)) {
            return;
        }
        WTFSocketRegistryItem item = WTFSocketMemCache.get(type, name);
        item.getChannel().close();
        WTFSocketMemCache.del(type, name);
    }
}
