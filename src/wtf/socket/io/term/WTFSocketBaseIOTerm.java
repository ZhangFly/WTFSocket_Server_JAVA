package wtf.socket.io.term;

import wtf.socket.WTFSocketServer;
import wtf.socket.io.WTFSocketIOTerm;

/**
 * 终端辅助基类
 *
 * Created by ZFly on 2017/4/22.
 */
public abstract class WTFSocketBaseIOTerm implements WTFSocketIOTerm {

    protected String ioTag = "0";
    protected String connectType = "TCP";
//    protected final WTFSocketServer context;

//    public WTFSocketBaseIOTerm(WTFSocketServer context) {
//        this.context = context;
//    }

//    @Override
//    public WTFSocketServer getContext() {
//        return context;
//    }

//    @Override
//    public void setContext(WTFSocketServer context) {
//    }

    @Override
    public String getIoTag() {
        return ioTag;
    }

    @Override
    public void setIoTag(String ioTag) {
        this.ioTag = ioTag;
    }

    @Override
    public String getConnectType() {
        return connectType;
    }

    @Override
    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

}
