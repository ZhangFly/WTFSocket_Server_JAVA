package wtf.socket.exception;

/**
 * 致命异常
 * 一般指协议无法被解析
 * 不可回溯发送源的异常
 *
 * Created by zfly on 2017/4/22.
 */
public abstract class WTFSocketFatalException extends WTFSocketException{
    public WTFSocketFatalException(String msg) {
        super(msg);
    }
}
