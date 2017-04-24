package wtf.apis;

import wtf.socket.protocol.WTFSocketMsg;

/**
 * 动作触发器
 * 当满足 when 方法是会触发相应的动作
 *
 * Created by zfly on 2017/4/21.
 */
public abstract class WTFSocketAPIsTrigger {

    private WTFSocketAPIsAction action;

    public abstract boolean when(WTFSocketMsg msg);

    WTFSocketAPIsAction getAction() {
        return action;
    }

    void setAction(WTFSocketAPIsAction action) {
        this.action = action;
    }

}
