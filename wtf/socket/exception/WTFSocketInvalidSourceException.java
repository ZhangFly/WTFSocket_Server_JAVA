package wtf.socket.exception;

/**
 * 无效的发送源异常
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketInvalidSourceException extends WTFSocketFatalException{

    public WTFSocketInvalidSourceException(String source) {
        super("Invalid source => <" + source + ">");
    }

    @Override
    public int getErrCode() {
        return 66;
    }
}
