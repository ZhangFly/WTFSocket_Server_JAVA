package wtf.socket.exception;


public class WTFSocketInvalidTargetException extends WTFSocketException {

    public WTFSocketInvalidTargetException(String target) {
        super("invalid target => <" + target + ">");
    }

    public int getErrCode() {
        return 16;
    }
}
