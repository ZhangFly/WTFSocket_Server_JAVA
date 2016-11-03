package wtf.apis;

import wtf.socket.protocols.templates.WTFSocketProtocol;

/**
 * WTFSocketAPIsManager 触发器
 */
public abstract class WTFSocketAPIsTrigger {

    public abstract boolean when(WTFSocketProtocol protocol);

    private WTFSocketAction action;

    WTFSocketAction getAction() {
        return action;
    }

    void setAction(WTFSocketAction action) {
        this.action = action;
    }

}
