package wtf.socket.routing.item;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.io.WTFSocketIOTerm;

import java.util.HashSet;
import java.util.Set;

/**
 * 正式客户端
 * <p>
 * Created by ZFly on 2017/4/23.
 */
public class WTFSocketRoutingFormalItem extends WTFSocketRoutingItem {

    /**
     * 授权通讯地址
     */
    private Set<String> authTargetsAddress;

    public WTFSocketRoutingFormalItem(WTFSocketRoutingItem item) {
        super(item);
    }

    public boolean isAuthTarget(String targetAddress) {
        return authTargetsAddress != null && authTargetsAddress.contains(targetAddress);
    }

    public void addAuthTarget(String targetAddress) {
        if (authTargetsAddress == null) {
            authTargetsAddress = new HashSet<String>() {{
                add("server");
            }};
        }
        authTargetsAddress.add(targetAddress);
    }

    public void close() throws WTFSocketException {
        super.close();
        getContext().getRouting().getFormalMap().remove(this);
    }

    public void removeAuthTarget(String targetAddress) {
        if (authTargetsAddress != null) {
            authTargetsAddress.remove(targetAddress);
        }
    }

}
