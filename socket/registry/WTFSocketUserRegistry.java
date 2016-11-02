package wtf.socket.registry;


import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户注册表
 */
public class WTFSocketUserRegistry {

    private static final ConcurrentHashMap<String, WTFSocketUserRegistryItem> registryTable = new ConcurrentHashMap<>(100);

    private static final ConcurrentHashMap<String, WTFSocketUserRegistryDebugItem> DEBUGS_TABLE = new ConcurrentHashMap<>();

    static {
        DEBUGS_TABLE.put("Debug_0", new WTFSocketUserRegistryDebugItem("Debug_0", null, "2.0", WTFSocketConnectType.TCP));
        DEBUGS_TABLE.put("Debug_1", new WTFSocketUserRegistryDebugItem("Debug_1", null, "2.0", WTFSocketConnectType.TCP));
        DEBUGS_TABLE.put("Debug_2", new WTFSocketUserRegistryDebugItem("Debug_2", null, "2.0", WTFSocketConnectType.TCP));
    }

    private WTFSocketUserRegistry() {
    }

    public static void register(final String name, final Channel ctx, String version, WTFSocketConnectType connectType, String deviceType) {
        if (isDebug(name)) {
            WTFSocketUserRegistryDebugItem debug = new WTFSocketUserRegistryDebugItem(name, ctx, version, connectType);
            DEBUGS_TABLE.put(name, debug);
        }else {
            WTFSocketUserRegistryItem user = new WTFSocketUserRegistryItem(name, ctx, version, connectType, deviceType);
            registryTable.put(name, user);
        }

    }

    public static void unRegister(final String name) {

        if (isDebug(name)) {
            DEBUGS_TABLE.put(name, new WTFSocketUserRegistryDebugItem(name, null, "2.0", WTFSocketConnectType.TCP));
        }else {
            if (registryTable.containsKey(name)) {
                registryTable.remove(name);
            }
        }
    }

    public static boolean contains(String name) {
        return registryTable.containsKey(name) || DEBUGS_TABLE.containsKey(name);
    }

    public static void clear() {
        registryTable.clear();
    }

    public static WTFSocketUserRegistryItem find(final String name) {
        return isDebug(name) ? DEBUGS_TABLE.get(name) : registryTable.get(name);
    }

    public static Collection<WTFSocketUserRegistryItem> findAll() {
        return registryTable.values();
    }

    public static boolean isDebug(String name) {
        return DEBUGS_TABLE.containsKey(name);
    }

    public static Collection<WTFSocketUserRegistryDebugItem> getRegisterDebugs() {
        return DEBUGS_TABLE.values();
    }
}
