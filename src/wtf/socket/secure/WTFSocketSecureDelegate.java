package wtf.socket.secure;

import wtf.socket.protocol.WTFSocketMsg;

/**
 * 安全代理
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@FunctionalInterface
public interface WTFSocketSecureDelegate {

    Object work(WTFSocketMsg msg);
}
