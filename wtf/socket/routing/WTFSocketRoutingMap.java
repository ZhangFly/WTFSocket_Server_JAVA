package wtf.socket.routing;

import wtf.socket.io.term.WTFSocketTerm;
import wtf.socket.io.term.impl.WTFSocketDefaultTerm;
import wtf.socket.routing.item.WTFSocketRoutingDebugItem;
import wtf.socket.routing.item.WTFSocketRoutingFormalItem;
import wtf.socket.routing.item.WTFSocketRoutingItem;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由表
 *
 * Created by zfly on 2017/4/22.
 */
public enum  WTFSocketRoutingMap {

    /**
     * 临时终端表
     * 记录连接上服务器但没有完成注册的终端
     */
    TMP(),
    /**
     * 调试终端表
     * 记录拥有调试权限的终端
     */
    DEBUG(),
    /**
     * 正式终端表
     * 记录拥有普通权限的终端
     */
    FORMAL() {{
        register(new WTFSocketRoutingFormalItem(new WTFSocketDefaultTerm()) {{
            // 默认添加 server 对象
            // server 对象代表服务器，不可被覆盖
            setCover(false);
            setAddress("server");
        }});
    }};

    private ConcurrentHashMap<String, WTFSocketRoutingItem> firstKey = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> secondKey = new ConcurrentHashMap<>();

    /**
     * 向路由表中注册对象
     * 如果对象地址不为空，则使用地址作为key
     * 如果对象地址为空，则使用ioTag作为key
     *
     * @param term 连接终端
     */
    public void register(WTFSocketTerm term) {
        if (this == TMP) {
            register(new WTFSocketRoutingTmpItem(term));
        }
    }


    /**
     * 向路由表中注册对象
     * 如果对象地址不为空，则使用地址作为key
     * 如果对象地址为空，则使用ioTag作为key
     *
     * @param item 路由表对象
     */
    public void register(WTFSocketRoutingItem item) {

        final String first = item.getTerm().getIoTag();
        final String second = item.getAddress();

        // 重复注册
        if (second != null && secondKey.containsKey(second)) {
            // 关闭原连接
            final String repeatKey = secondKey.get(second);
            final WTFSocketRoutingItem repeatItem = firstKey.get(repeatKey);

            if (repeatItem.isCover()) {
                repeatItem.getTerm().close();
                firstKey.remove(repeatKey);
                secondKey.put(second, first);
                firstKey.put(first, item);
            }
        }else {
            if (second != null) {
                secondKey.put(second, first);
            }
            firstKey.put(first, item);
        }
    }

    /**
     * 路由表注销对象
     *
     * @param item 路由表对象
     */
    public void unRegister(WTFSocketRoutingItem item) {

        final String first = item.getTerm().getIoTag();
        final String second = this == TMP ? null : item.getAddress();

        if (firstKey.containsKey(first)) {
            firstKey.remove(first);
        }
        if (second != null && secondKey.containsKey(second)) {
            secondKey.remove(second);
        }
    }

    /**
     * 路由表是否包含对象
     *
     * @param key 键
     * @return 是否包含对象
     */
    public boolean contains(String key) {
        return firstKey.containsKey(key) || secondKey.containsKey(key);
    }

    /**
     * 获取表中的一个对象
     *
     * @param key 键
     * @return 路由表对象
     */
    public WTFSocketRoutingItem getItem(String key) {
        WTFSocketRoutingItem item = firstKey.get(key);
        if (item == null && secondKey.containsKey(key)) {
            item = firstKey.get(secondKey.get(key));
        }
        return item;
    }

    /**
     * 将对象移动到另一个路由表
     * 只能有TMP表向FORMAL获取DEBUG表移动
     *
     * @param item 路由表对象
     * @param dst 目的路由表
     */
    public void shift(WTFSocketRoutingItem item, WTFSocketRoutingMap dst) {
        if (this != TMP) {
            return;
        }
        if (dst == FORMAL) {
            dst.register(new WTFSocketRoutingFormalItem(item));
        }
        unRegister(item);
        if (dst == DEBUG) {
            dst.register(new WTFSocketRoutingDebugItem(item));
        }
    }

    /**
     * 获取表中的所有对象
     *
     * @return 表中的所有对象
     */
    public Collection<WTFSocketRoutingItem> mapValues() {
        return firstKey.values();
    }
}
