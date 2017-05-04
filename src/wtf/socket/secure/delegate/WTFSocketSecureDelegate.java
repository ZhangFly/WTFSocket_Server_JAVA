package wtf.socket.secure.delegate;

import wtf.socket.protocol.WTFSocketMessage;

/**
 * 安全代理
 * <p>
 * Created by ZFly on 2017/4/25.
 */
@FunctionalInterface
public interface WTFSocketSecureDelegate {

    Object work(WTFSocketMessage msg);
}
