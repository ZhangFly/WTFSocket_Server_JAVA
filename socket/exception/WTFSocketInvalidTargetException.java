package wtf.socket.exception;


public class WTFSocketInvalidTargetException extends WTFSocketException {

    private String target;

    public WTFSocketInvalidTargetException(String target) {
        super("invalid target => <" + target + ">");
        this.target = target;
    }

    public int getErrCode() {
        return 16;
    }

    public String getTarget() {
        return target;
    }
}
