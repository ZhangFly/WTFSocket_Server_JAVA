package wtf.socket.exception;

public abstract class WTFSocketException extends Exception{

    public WTFSocketException(String msg) {
        super(msg);
    }

    public abstract int getErrCode();

}
