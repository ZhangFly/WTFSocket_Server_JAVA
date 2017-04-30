package wtf.socket.exception.fatal;

/**
 * 无效的发送源异常
 * 包括发送源为注册，或者发送源通讯地址和连接地址不匹配
 * <p>
 * Created by ZFly on 2017/4/22.
 */
public class WTFSocketInvalidSourceException extends WTFSocketFatalException {

    public WTFSocketInvalidSourceException(String msg) {
        super(msg);
    }

    @Override
    public int getErrCode() {
        return 66;
    }
}
