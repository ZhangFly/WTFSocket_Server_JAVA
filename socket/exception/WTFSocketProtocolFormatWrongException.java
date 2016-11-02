package wtf.socket.exception;

public class WTFSocketProtocolFormatWrongException extends WTFSocketException {

    public WTFSocketProtocolFormatWrongException(String msg) {
        super("protocol format wrong => <" + msg + ">");
    }

    public int getErrCode() {
        return 64;
    }
}
