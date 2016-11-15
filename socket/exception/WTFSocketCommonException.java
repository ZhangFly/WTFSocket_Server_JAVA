package wtf.socket.exception;

import wtf.socket.protocols.templates.WTFSocketProtocol;

public abstract class WTFSocketCommonException extends WTFSocketException{

    private WTFSocketProtocol original;

    public WTFSocketCommonException(String msg) {
        super(msg);
    }

    public WTFSocketProtocol getOriginal() {
        return original;
    }

    public void setOriginal(WTFSocketProtocol original) {
        this.original = original;
    }
}
