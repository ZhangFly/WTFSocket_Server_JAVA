package wtf.apis;

import wtf.socket.protocols.templates.WTFSocketProtocol;

/**
 * WTFSocketAPIsManager 触发器
 */
public abstract class WTFSocketAPIsTrigger {

    public abstract boolean when(WTFSocketProtocol protocol);

    private WTFSocketAPIsAction action;

    WTFSocketAPIsAction getAction() {
        return action;
    }

    void setAction(WTFSocketAPIsAction action) {
        this.action = action;
    }

}
