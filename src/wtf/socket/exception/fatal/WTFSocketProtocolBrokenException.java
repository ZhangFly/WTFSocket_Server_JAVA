package wtf.socket.exception.fatal;

/**
 * 协议格式错误
 * 一般是约定了某种格式如JSON
 * 但实际格式不符合
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketProtocolBrokenException extends WTFSocketFatalException {

    public WTFSocketProtocolBrokenException(String msg) {
        super("Protocol broken => <" + msg + ">");
    }

    public int getErrCode() {
        return 64;
    }
}
