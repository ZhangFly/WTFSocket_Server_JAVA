package wtf.socket.secure;

import wtf.socket.protocol.WTFSocketMsg;

/**
 *
 * Created by zfly on 2017/4/25.
 */
@FunctionalInterface
public interface WTFSocketSecureDelegate {

    Object work(WTFSocketMsg msg);
}
