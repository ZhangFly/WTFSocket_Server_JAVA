package wtf.socket.registry.items;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

import java.util.ArrayList;
import java.util.List;

/**
 * 调试账号注册表条目
 */
public class WTFSocketRegistryDebugItem extends WTFSocketRegistryUserItem {

    private List<String> filters = new ArrayList<>();
    private boolean isShowHeartbeatMsg = false;

    public WTFSocketRegistryDebugItem(final String name, final Channel channel, final WTFSocketConnectType connectType, final String accept) {
        super(name, channel, connectType, accept, "Debug");
    }

    public void addFilterGrep(final String grep) {
        filters.add(grep);
    }

    public void removeFilterGrep(String grep) {
        if (filters.contains(grep)) {
            filters.remove(grep);
        }
    }

    public boolean isFilter(String msg) {
        if (filters.isEmpty()) {
            return true;
        }
        boolean flag = false;
        for (String grep : filters) {
            flag = flag || msg.contains("<" + grep + ">");
        }
        return flag;
    }

    public void clearFilterGreps() {
        filters.clear();
    }

    public boolean isShowHeartbeatMsg() {
        return isShowHeartbeatMsg;
    }

    public void setShowHeartbeatMsg(boolean showHeartbeatMsg) {
        isShowHeartbeatMsg = showHeartbeatMsg;
    }
}
