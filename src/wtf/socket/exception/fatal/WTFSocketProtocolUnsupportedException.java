package wtf.socket.exception.fatal;

/**
 * 不被支持的协议
 * 没有向协议族注册相应的解析器
 * <p>
 * Created by ZFly on 2017/4/22.
 */
public class WTFSocketProtocolUnsupportedException extends WTFSocketFatalException {

    public WTFSocketProtocolUnsupportedException(String msg) {
        super(msg);
    }

    public int getErrCode() {
        return 65;
    }
}
