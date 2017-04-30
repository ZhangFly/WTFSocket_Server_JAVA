package wtf.socket.routing;

import wtf.socket.routing.item.WTFSocketRoutingItem;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由表
 * <p>
 * Created by ZFly on 2017/4/22.
 */
public class WTFSocketRoutingItemMap<T extends WTFSocketRoutingItem> {

    private ConcurrentHashMap<String, T> ioAddressMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> communicationAddressMap = new ConcurrentHashMap<>();

    /**
     * 向路由表中注册对象
     * 如果对象地址不为空，则使用地址作为key
     * 如果对象地址为空，则使用ioTag作为key
     *
     * @param newItem 路由表对象
     */
    public void add(T newItem) {

        final String newIOAddress = newItem.getTerm().getIoTag();
        final String newCommunicationAddress = newItem.getAddress();

        // 如果通讯地址已被注册
        if (newCommunicationAddress != null && communicationAddressMap.containsKey(newCommunicationAddress)) {
            final String oldIOAddress = communicationAddressMap.get(newCommunicationAddress);
            final WTFSocketRoutingItem oldItem = ioAddressMap.get(oldIOAddress);
            // 原客户端允许覆盖
            // 则关闭原客户端连接，并替换为新连接
            if (oldItem.isCover()) {
                oldItem.getTerm().close();
                ioAddressMap.remove(oldIOAddress);
                ioAddressMap.put(newIOAddress, newItem);
                communicationAddressMap.put(newCommunicationAddress, newIOAddress);
            }
        } else {
            ioAddressMap.put(newIOAddress, newItem);
            if (newCommunicationAddress != null) {
                communicationAddressMap.put(newCommunicationAddress, newIOAddress);
            }
        }
    }

    /**
     * 路由表注销对象
     *
     * @param item 路由表对象
     */
    public void remove(WTFSocketRoutingItem item) {
        final String ioAddress = item.getTerm().getIoTag();
        final String communicationAddress = item.getAddress();

        if (ioAddressMap.containsKey(ioAddress)) {
            ioAddressMap.remove(ioAddress);
        }
        if (communicationAddress != null && communicationAddressMap.containsKey(communicationAddress)) {
            communicationAddressMap.remove(communicationAddress);
        }
    }

    /**
     * 路由表是否包含对象
     *
     * @param address 键
     *
     * @return 是否包含对象
     */
    public boolean contains(String address) {
        return ioAddressMap.containsKey(address) || communicationAddressMap.containsKey(address);
    }

    /**
     * 获取表中的一个对象
     *
     * @param address 键
     *
     * @return 路由表对象
     */
    public T getItem(String address) {
        T item = ioAddressMap.get(address);
        if (item == null && communicationAddressMap.containsKey(address)) {
            item = ioAddressMap.get(communicationAddressMap.get(address));
        }
        return item;
    }

    /**
     * 获取表中的所有对象
     *
     * @return 表中的所有对象
     */
    public Collection<T> values() {
        return ioAddressMap.values();
    }
}
