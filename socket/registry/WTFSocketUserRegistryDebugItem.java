package wtf.socket.registry;

import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketConnectType;

import java.util.ArrayList;
import java.util.List;


public class WTFSocketUserRegistryDebugItem extends WTFSocketUserRegistryItem{

    private List<String> filters = new ArrayList<>();
    private boolean isShowHeartbeatMsg = false;

    WTFSocketUserRegistryDebugItem(String name, Channel channel, String accept, WTFSocketConnectType connectType) {
        super(name, channel, accept, connectType, "Debug");
    }

    public void addFilter(final String name) {
        filters.add(name);
    }

    public void removeFilter(String name) {
        if (filters.contains(name)) {
            filters.remove(name);
        }
    }

    public boolean filter(String msg) {

        if (filters.isEmpty()) {
            return true;
        }

        boolean flag = false;

        for (String grep : filters) {
            flag = flag || msg.contains("<" + grep + ">");
        }

        return flag;
    }

    public void clearFilter() {
        filters.clear();
    }

    public boolean isShowHeartbeatMsg() {
        return isShowHeartbeatMsg;
    }

    public void setShowHeartbeatMsg(boolean showHeartbeatMsg) {
        isShowHeartbeatMsg = showHeartbeatMsg;
    }
}
