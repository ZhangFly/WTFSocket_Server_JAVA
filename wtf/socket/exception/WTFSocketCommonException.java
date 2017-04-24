package wtf.socket.exception;

import wtf.socket.protocol.WTFSocketMsg;

/**
 * 普通异常
 * 一般指协议已被正常解析
 * 可回溯发送源的异常
 *
 * Created by zfly on 2017/4/22.
 */
public abstract class WTFSocketCommonException extends WTFSocketException{

    /**
     * 造成异常的源数据
     */
    private WTFSocketMsg originalMsg;

    public WTFSocketCommonException(String msg) {
        super(msg);
    }

    public WTFSocketMsg getOriginalMsg() {
        return originalMsg;
    }

    public WTFSocketCommonException setOriginalMsg(WTFSocketMsg originalMsg) {
        this.originalMsg = originalMsg;
        return this;
    }
}
