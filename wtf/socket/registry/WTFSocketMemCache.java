package wtf.socket.registry;

import wtf.socket.registry.items.WTFSocketRegistryItem;
import wtf.socket.registry.items.WTFSocketUserType;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis辅助方法
 */
class WTFSocketMemCache {

    private static final ConcurrentHashMap<String, WTFSocketRegistryItem> userTable = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, WTFSocketRegistryItem> debugTable = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, WTFSocketRegistryItem> tmpTable = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, WTFSocketRegistryItem> selectTable(final WTFSocketUserType type) {
        switch (type) {
            case USER:
                return userTable;
            case DEBUG:
                return debugTable;
            case TMP:
                return tmpTable;
            default:
                return new ConcurrentHashMap<>();
        }
    }

    static void set(final WTFSocketUserType type, final String key, final WTFSocketRegistryItem item) {
        if (item == null) {
            return;
        }
        selectTable(type).put(key, item);
    }

    static WTFSocketRegistryItem get(final WTFSocketUserType type, final String key) {
        if (!selectTable(type).containsKey(key)) {
            return null;
        }
        return selectTable(type).get(key);
    }

    static void del(final WTFSocketUserType type, final String key) {
        selectTable(type).remove(key);
    }

    static boolean contains(final WTFSocketUserType type, final String key) {
        return selectTable(type).containsKey(key);
    }

    static Collection<WTFSocketRegistryItem> values(final WTFSocketUserType type) {
        return selectTable(type).values();
    }
}
