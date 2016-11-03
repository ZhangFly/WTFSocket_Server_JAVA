package wtf.apis;

import wtf.socket.protocols.templates.WTFSocketProtocol;

/**
 * APIsManager 触发器
 */
public abstract class APIsTrigger {

    public abstract boolean when(WTFSocketProtocol protocol);

    private Action action;

    Action getAction() {
        return action;
    }

    void setAction(Action action) {
        this.action = action;
    }

}
