package wtf.socket.secure;

import wtf.socket.WTFSocketServer;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 * 安全检查策略组接口
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public interface WTFSocketSecureCheck {

    boolean check(WTFSocketServer context, WTFSocketMsg msg) throws WTFSocketException;

}
