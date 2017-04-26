package wtf.socket.exception;

/**
 * 框架异常
 *
 * Created by zfly on 2017/4/22.
 */
public abstract class WTFSocketException extends Exception{

    public WTFSocketException(String msg) {
        super(msg);
    }

    public abstract int getErrCode();

}
