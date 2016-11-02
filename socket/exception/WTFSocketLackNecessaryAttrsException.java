package wtf.socket.exception;

public class WTFSocketLackNecessaryAttrsException extends WTFSocketException {

    public WTFSocketLackNecessaryAttrsException(String attr) {
        super("lack necessary attr => <" + attr + ">");
    }

    public int getErrCode() {
        return 66;
    }
}
