package wtf.socket.exception;

/**
 * 服务器异常
 * <p>
 * Created by ZFly on 2017/4/22.
 */
public abstract class WTFSocketException extends Exception {

    private String msg;

    public WTFSocketException(String msg) {
        super(msg);
        this.msg = getClass().getSimpleName() + ":  " + msg;
    }

    public abstract int getErrCode();

    @Override
    public String getMessage() {
        return msg;
    }
}
