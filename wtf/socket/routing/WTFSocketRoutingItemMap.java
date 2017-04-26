package wtf.socket.routing;

import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由表
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketRoutingItemMap<T extends WTFSocketRoutingItem>{

    private ConcurrentHashMap<String, T> firstKeys = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> secondKeys = new ConcurrentHashMap<>();

    /**
     * 向路由表中注册对象
     * 如果对象地址不为空，则使用地址作为key
     * 如果对象地址为空，则使用ioTag作为key
     *
     * @param item 路由表对象
     */
    public void add(T item) {

        final String first = item.getTerm().getIoTag();
        final String second = item.getAddress();

        // 重复注册
        if (second != null && secondKeys.containsKey(second)) {
            // 关闭原连接
            final String repeatKey = secondKeys.get(second);
            final WTFSocketRoutingItem repeatItem = firstKeys.get(repeatKey);

            if (repeatItem.isCover()) {
                repeatItem.getTerm().close();
                firstKeys.remove(repeatKey);
                secondKeys.put(second, first);
                firstKeys.put(first, item);
            }
        }else {
            if (second != null) {
                secondKeys.put(second, first);
            }
            firstKeys.put(first, item);
        }
    }

    /**
     * 路由表注销对象
     *
     * @param item 路由表对象
     */
    public void remove(WTFSocketRoutingItem item) {
        final String first = item.getTerm().getIoTag();
        final String second = item.getAddress();

        if (firstKeys.containsKey(first)) {
            firstKeys.remove(first);
        }
        if (second != null && secondKeys.containsKey(second)) {
            secondKeys.remove(second);
        }
    }

    /**
     * 路由表是否包含对象
     *
     * @param key 键
     * @return 是否包含对象
     */
    public boolean contains(String key) {
        return firstKeys.containsKey(key) || secondKeys.containsKey(key);
    }

    /**
     * 获取表中的一个对象
     *
     * @param key 键
     * @return 路由表对象
     */
    public T getItem(String key) {
        T item = firstKeys.get(key);
        if (item == null && secondKeys.containsKey(key)) {
            item = firstKeys.get(secondKeys.get(key));
        }
        return item;
    }

    /**
     * 获取表中的所有对象
     *
     * @return 表中的所有对象
     */
    public Collection<T> values() {
        return firstKeys.values();
    }
}
