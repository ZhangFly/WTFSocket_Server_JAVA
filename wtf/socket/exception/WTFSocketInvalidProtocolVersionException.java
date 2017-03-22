package wtf.socket.exception;

public class WTFSocketInvalidProtocolVersionException extends WTFSocketFatalException {

    public WTFSocketInvalidProtocolVersionException(String version) {
        super("invalid protocol version => <" + version + ">");
    }

    public int getErrCode() {
        return 65;
    }
}
