package wtf.socket.secure.strategy;

import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 安全策略接口
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public interface WTFSocketSecureStrategy {

    void check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException;

    void setNext(WTFSocketSecureStrategy next);

}
