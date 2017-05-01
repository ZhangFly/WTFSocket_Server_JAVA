package wtf.socket.routing.item;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.exception.WTFSocketException;

import java.util.HashSet;
import java.util.Set;

/**
 * 正式客户端
 * <p>
 * Created by ZFly on 2017/4/23.
 */
@Component
@Scope("prototype")
public class WTFSocketRoutingFormalItem extends WTFSocketRoutingItem {

    /**
     * 授权通讯地址
     */
    private Set<String> authTargetsAddress;

    public boolean isAuthTarget(String targetAddress) {
        return authTargetsAddress != null && (authTargetsAddress.contains("*") || authTargetsAddress.contains(targetAddress));
    }

    public void addAuthTarget(String targetAddress) {
        if (authTargetsAddress == null) {
            authTargetsAddress = new HashSet<String>() {{
                add("server");
            }};
        }
        authTargetsAddress.add(targetAddress);
    }

    public void removeAuthTarget(String targetAddress) {
        if (authTargetsAddress != null) {
            authTargetsAddress.remove(targetAddress);
        }
    }

    public void close() throws WTFSocketException {
        super.close();
        getContext().getRouting().getFormalMap().remove(this);
    }
}
