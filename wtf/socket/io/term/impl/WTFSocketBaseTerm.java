package wtf.socket.io.term.impl;

import wtf.socket.io.term.WTFSocketTerm;
import wtf.socket.protocol.WTFSocketConnectType;

/**
 * 终端辅助基类
 *
 * Created by zfly on 2017/4/22.
 */
public abstract class WTFSocketBaseTerm implements WTFSocketTerm{

    protected String ioTag = "0";
    protected WTFSocketConnectType connectType = WTFSocketConnectType.TCP;

    @Override
    public String getIoTag() {
        return ioTag;
    }

    @Override
    public void setIoTag(String ioTag) {
        this.ioTag = ioTag;
    }

    @Override
    public WTFSocketConnectType getConnectType() {
        return connectType;
    }

    @Override
    public void setConnectType(WTFSocketConnectType connectType) {
        this.connectType = connectType;
    }

}
