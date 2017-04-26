package wtf.socket.secure;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 *
 * Created by zfly on 2017/4/25.
 */
public interface WTFSocketSecureStrategy {

    void invoke(WTFSocketMsg msg) throws WTFSocketException;

}
