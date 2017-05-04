package wtf.socket.routing.client;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 调试者客户端
 * <p>
 * Created by ZFly on 2017/4/23.
 */
@Component
@Scope("prototype")
public class WTFSocketDebugClient extends WTFSocketFormalClient {

    /**
     * 拦截器规则
     */
    private List<String> filterGreps;

    WTFSocketDebugClient() {
        addAuthTarget("server");
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

    public void close() {
        super.close();
        getContext().getRouting().getDebugMap().remove(this);
    }

    public void clearFilterGreps() {
        filterGreps = null;
    }
}
