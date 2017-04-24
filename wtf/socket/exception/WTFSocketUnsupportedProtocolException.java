package wtf.socket.exception;

/**
 * 不被支持的协议
 * 没有向协议族注册相应的解析器
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketUnsupportedProtocolException extends WTFSocketFatalException {

    public WTFSocketUnsupportedProtocolException(String msg) {
        super("Unsupported message => <" + msg + ">");
    }

    public int getErrCode() {
        return 65;
    }
}
