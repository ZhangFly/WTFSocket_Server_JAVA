package wtf.socket.exception;

/**
 * 无效的目标异常
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketInvalidTargetException extends WTFSocketCommonException {

    public WTFSocketInvalidTargetException(String target) {
        super("Invalid target => <" + target + ">");
    }

    public int getErrCode() {
        return 16;
    }

}
