package wtf.socket.exception.fatal;

/**
 * 使用了系统保留字
 * <p>
 * Created by ZFly on 2017/4/25.
 */
public class WTFSocketKeepWordsException extends WTFSocketFatalException {
    public WTFSocketKeepWordsException(String msg) {
        super(msg);
    }

    @Override
    public int getErrCode() {
        return 69;
    }
}
