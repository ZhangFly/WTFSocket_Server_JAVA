package wtf.socket.routing.item;

import wtf.socket.io.term.WTFSocketTerm;

import java.util.ArrayList;
import java.util.List;

/**
 * 调试者对象
 *
 * Created by zfly on 2017/4/23.
 */
public class WTFSocketRoutingDebugItem extends WTFSocketRoutingItem{

    /**
     * 拦截器规则
     */
    private List<String> filterGreps;
    /**
     * 是否显示心跳包
     */
    private boolean isShowHeartbeatMsg = false;

    public WTFSocketRoutingDebugItem(WTFSocketTerm term) {
        super(term);
    }

    public WTFSocketRoutingDebugItem(WTFSocketRoutingItem item) {
        super(item);
    }

    public void addFilterGrep(String grep) {
        if (filterGreps == null) {
            filterGreps = new ArrayList<>();
        }
        filterGreps.add(grep);
    }

    public void removeFilterGrep(String grep) {
        if (filterGreps != null) {
            filterGreps.remove(grep);
        }
    }

    public boolean isFilter(String msg) {
        if (filterGreps == null || filterGreps.isEmpty()) {
            return true;
        }
        boolean flag = false;
        for (String grep : filterGreps) {
            flag = flag || msg.contains("<" + grep + ">");
        }
        return flag;
    }

    public void clearFilterGreps() {
        filterGreps = null;
    }

    public boolean isShowHeartbeatMsg() {
        return isShowHeartbeatMsg;
    }

    public void setShowHeartbeatMsg(boolean showHeartbeatMsg) {
        isShowHeartbeatMsg = showHeartbeatMsg;
    }
}
