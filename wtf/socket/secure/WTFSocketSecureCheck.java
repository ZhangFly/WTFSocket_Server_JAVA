package wtf.socket.secure;

import wtf.socket.exception.WTFSocketException;
import wtf.socket.protocol.WTFSocketMsg;

/**
 *
 * Created by zfly on 2017/4/25.
 */
public interface WTFSocketSecureCheck  {

    boolean check(WTFSocketMsg msg) throws WTFSocketException;

}
