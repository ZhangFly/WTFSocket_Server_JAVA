package wtf.socket.routing.item;

import wtf.socket.exception.WTFSocketException;

import java.util.ArrayList;
import java.util.List;

/**
 * 调试者客户端
 * <p>
 * Created by ZFly on 2017/4/23.
 */
public class WTFSocketRoutingDebugItem extends WTFSocketRoutingItem {

    /**
     * 拦截器规则
     */
    private List<String> filterGreps;

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

    public void close() throws WTFSocketException {
        super.close();
        getContext().getRouting().getDebugMap().remove(this);
    }

    public void clearFilterGreps() {
        filterGreps = null;
    }
}
