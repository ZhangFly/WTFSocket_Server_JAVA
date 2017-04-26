package wtf.socket.routing.item;

import wtf.socket.WTFSocket;
import wtf.socket.io.WTFSocketIOTerm;

import java.util.HashSet;
import java.util.Set;

/**
 * 正式对象
 */
public class WTFSocketRoutingFormalItem extends WTFSocketRoutingItem {

    /**
     * 授权通讯地址
     */
    private Set<String> authTargetsAddress;

    public WTFSocketRoutingFormalItem(WTFSocketIOTerm term) {
        super(term);
    }

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

    public void logout() {
        super.logout();
        WTFSocket.ROUTING.FORMAL_MAP.remove(this);
    }

    public void removeAuthTarget(String targetAddress) {
        if (authTargetsAddress != null) {
            authTargetsAddress.remove(targetAddress);
        }
    }

}
