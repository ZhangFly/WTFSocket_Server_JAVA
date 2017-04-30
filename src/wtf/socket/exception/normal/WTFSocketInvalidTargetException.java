package wtf.socket.exception.normal;

/**
 * 无效的目标异常
 *
 * Created by ZFly on 2017/4/22.
 */
public class WTFSocketInvalidTargetException extends WTFSocketNormalException {

    public WTFSocketInvalidTargetException(String msg) {
        super(msg);
    }

    public int getErrCode() {
        return 16;
    }

}
