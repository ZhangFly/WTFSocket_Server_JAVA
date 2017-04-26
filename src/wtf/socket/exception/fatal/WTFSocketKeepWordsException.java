package wtf.socket.exception.fatal;

/**
 *
 * Created by zfly on 2017/4/25.
 */
public class WTFSocketKeepWordsException extends WTFSocketFatalException {
    public WTFSocketKeepWordsException(String msg) {
        super("Keep words => <" + msg + ">");
    }

    @Override
    public int getErrCode() {
        return 69;
    }
}
