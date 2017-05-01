package wtf.socket.secure.delegate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.event.WTFSocketEventsType;

import java.util.HashMap;
import java.util.Map;

/**
 * 安全组件
 * <p>
 * Created by ZFly on 2017/4/24.
 */
@Component
@Scope("prototype")
public class WTFSocketSecureDelegatesGroup {

    private final Map<WTFSocketSecureDelegateType, WTFSocketSecureDelegate> group = new HashMap<>(WTFSocketEventsType.values().length);

    /**
     * 安全代理
     *
     * @param delegate 代理
     * @param type     代理类型
     */
    public void addDelegate(WTFSocketSecureDelegate delegate, WTFSocketSecureDelegateType type) {
        group.put(type, delegate);
    }

    /**
     * 移除安全代理
     *
     * @param type 代理类型
     */
    public void removeDelegate(WTFSocketSecureDelegateType type) {
        group.remove(type);
    }

    /**
     * 获取安全代理
     *
     * @param type 代理类型
     *
     * @return 代理
     */
    public WTFSocketSecureDelegate getDelegate(WTFSocketSecureDelegateType type) {
        return group.get(type);
    }
}
